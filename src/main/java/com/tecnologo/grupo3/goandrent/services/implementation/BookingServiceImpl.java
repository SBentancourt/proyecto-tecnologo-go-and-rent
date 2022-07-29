package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.*;
import com.tecnologo.grupo3.goandrent.dtos.responses.*;
import com.tecnologo.grupo3.goandrent.entities.Accommodation;
import com.tecnologo.grupo3.goandrent.entities.Booking;
import com.tecnologo.grupo3.goandrent.entities.Gallery;
import com.tecnologo.grupo3.goandrent.entities.Review;
import com.tecnologo.grupo3.goandrent.entities.users_types.Guest;
import com.tecnologo.grupo3.goandrent.exceptions.UserException;
import com.tecnologo.grupo3.goandrent.repositories.BookingRepository;
import com.tecnologo.grupo3.goandrent.services.*;
import com.tecnologo.grupo3.goandrent.utils.enums.BookingStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.tecnologo.grupo3.goandrent.services.implementation.AccommodationServiceImpl.listConvertToPage1;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private GuestService guestService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserCalificationService userCalificationService;

    @Override
    public Boolean existBookingsDateRange(VerifyBookingDTO verifyBookingDTO) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = format.parse(verifyBookingDTO.getStart_date());
        Date endDate = format.parse(verifyBookingDTO.getEnd_date());
        return bookingRepository.existsBookingsInDateRange(verifyBookingDTO.getId_accommodation(), startDate,
                                                            endDate, BookingStatus.ACEPTADA, BookingStatus.PENDIENTE);
    }

    @Override
    public int generateBooking(BookingDataDTO bookingDataDTO) throws ParseException {
        // -- calcular cantidad de dias de reserva (solo noches)
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date startDate = format.parse(bookingDataDTO.getStart_date());
        Date endDate = format.parse(bookingDataDTO.getEnd_date());
        int nights = (int) ((endDate.getTime() - startDate.getTime()) / 86400000);

        // -- obtener alojamiento y precio final
        Accommodation accommodation = accommodationService.getAccommodationObject(bookingDataDTO.getIdAccommodation());
        float totalPrice = accommodation.getPrice() * nights;

        // -- obtener el usuario huésped
        Guest guest = guestService.getGuestByAlias(bookingDataDTO.getAliasGuest())
                .orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "Usuario no encontrado con el alias: " + bookingDataDTO.getAliasGuest()));
        // -- crear reserva con estado pendiente
        Booking booking = bookingRepository.save(new Booking(startDate, endDate, BookingStatus.PENDIENTE, PaymentStatus.PENDIENTE, totalPrice, accommodation, guest));

        return booking.getId();

    }

    @Override
    public Booking getBookingObject(int id) {
        return bookingRepository.getById(id);
    }

    @Override
    public void updateBooking(Booking booking) {
        bookingRepository.save(booking);
    }

    @Override
    public Double getFinalPriceBooking(int idAccommodation, String startDate, String endDate) throws ParseException {
        // -- calcular cantidad de dias de reserva (solo noches)
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date start_date = format.parse(startDate);
        Date end_date = format.parse(endDate);
        int nights = (int) ((end_date.getTime() - start_date.getTime()) / 86400000);
        // -- obtener alojamiento y precio final
        Accommodation accommodation = accommodationService.getAccommodationObject(idAccommodation);
        return Double.valueOf(accommodation.getPrice() * nights);
    }

    @Override
    public String getAuthorizationIdPayment(int id) {
        return bookingRepository.getById(id).getPaypalPayment().getAuthorization();
    }

    @Override
    public Float getTotalPriceBooking(int idBooking) {
        return bookingRepository.getById(idBooking).getFinalPrice();
    }

    @Override
    public void updatePaymentStatus(int idBooking, PaymentStatus status) {
        Booking booking = bookingRepository.getById(idBooking);
        booking.setPaymentStatus(status);
        bookingRepository.save(booking);
    }

    @Override
    public void updateBookingStatus(int idBooking, BookingStatus status) {
        Booking booking = bookingRepository.getById(idBooking);
        booking.setBookingStatus(status);
        bookingRepository.save(booking);
    }

    @Override
    public String getOrderId(int idBooking) {
        return bookingRepository.getById(idBooking).getPaypalPayment().getOrderId();
    }

    @Override
    public Boolean guestWithBookingsInProgress(String guestAlias) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());
        Date today = format.parse(fecha);
        return bookingRepository.guestWithBookingsInProgress(guestAlias, today, BookingStatus.ACEPTADA, BookingStatus.PENDIENTE);
    }

    @Override
    public Boolean hostWithBookingsInProgress(String aliasHost) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());
        Date today = format.parse(fecha);
        return bookingRepository.hostWithBookingsInProgress(aliasHost, today, BookingStatus.ACEPTADA, BookingStatus.PENDIENTE);
    }

    @Override
    public List<ReviewInfoDTO> getBookingsInfoAndReviewsByAccommodationId(int id) {
        List<Booking> bookings = bookingRepository.findBookingsByAccommodation_Id(id);
        List<ReviewInfoDTO> reviewInfoDTOS = new ArrayList<>();
        for (Booking b: bookings){
            ReviewInfoDTO reviewInfoDTO = new ReviewInfoDTO(b.getReview().getQualification(), b.getReview().getDescription(),
                                                            b.getGuest().getName(), b.getGuest().getPicture());
            reviewInfoDTOS.add(reviewInfoDTO);
        }
        return reviewInfoDTOS;
    }

    @Override
    public Boolean accommodationWithBookings(int id) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = format.format(new Date());
        Date today = format.parse(fecha);
        return bookingRepository.accommodationWithBookings(id, today, BookingStatus.ACEPTADA, BookingStatus.PENDIENTE);
    }

    @Override
    public HostBookingsListResponse getHostBookings(String alias, MultiValueMap<String, String> filtros, int nroPag, int cantReg) throws ParseException {
        List<Accommodation> accommodations = accommodationService.getAccommodationsByHostAliasBookings(alias);
        List<HostBookingsResponse> responses = new ArrayList<>();
        List<Integer> posiciones = new ArrayList<>();
        // -- primero me quedo con los alojamientos válidos.
        if (filtros.containsKey("acc_name")) {
            String acc_name_filter = filtros.getFirst("acc_name");
            for (Accommodation a : accommodations) {
                if (!acc_name_filter.equals(a.getName())){
                    posiciones.add(accommodations.indexOf(a));
                }
            }
        }
        if (posiciones.size() > 0) {
            posiciones.sort((o1, o2) -> o2.compareTo(o1));
            for (Integer i : posiciones) {
                accommodations.remove(i.intValue());
            }
            posiciones.clear();
        }

        // -- De los alojamientos válidos me quedo con las reservas válidas
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        if (filtros.containsKey("booking_status") || (filtros.containsKey("start_date") && filtros.containsKey("end_date")) ){
            for (Accommodation a : accommodations) {
                List<Booking> bookings = bookingRepository.findBookingsByAccommodation_Id(a.getId());
                for (Booking b: bookings){
                    Boolean reservaValida = true;
                    if (filtros.containsKey("booking_status")){
                        if (!b.getBookingStatus().toString().equals(filtros.getFirst("booking_status"))){
                            posiciones.add(bookings.indexOf(b));
                            reservaValida = false;
                        }
                    }
                    if (reservaValida && filtros.containsKey("start_date") && filtros.containsKey("end_date")){
                        Date startDate = format.parse(filtros.getFirst("start_date"));
                        Date endDate = format.parse(filtros.getFirst("end_date"));
                        // -- si la fecha de inicio de la reserva es menor o igual a la ingresada en el filtro y
                        // -- la fecha de fin de la reserva es menor o igual a la ingresada en el filtro.
                        // -- Negado! Me interesa saber cuando no se cumple la premisa anterior.
                        /*if ( !((b.getStartDate().equals(startDate) || startDate.before(b.getStartDate())) &&
                                (b.getEndDate().equals(endDate) || endDate.after(b.getEndDate()))) ){
                                posiciones.add(bookings.indexOf(b));
                        }*/
                        if (b.getStartDate().before(startDate) || b.getEndDate().after(endDate)){
                            posiciones.add(bookings.indexOf(b));
                        }
                    }
                }

                // -- cuando termino de recorrer las reservas actualizo el array de las reservas y voy agregando los registros a devolver
                if (posiciones.size() > 0) {
                    posiciones.sort((o1, o2) -> o2.compareTo(o1));
                    for (Integer i : posiciones) {
                        bookings.remove(i.intValue());
                    }
                    posiciones.clear();
                }

                // -- estas son las reservas validas del alojamiento que estoy procesando
                for (Booking b: bookings){
                    String  guestAlias = b.getGuest().getAlias();
                    Integer guestQualification = userCalificationService.getUserQualificationByQualifyingAndQualifiedUsers(alias, guestAlias);
                    responses.add(new HostBookingsResponse(b.getId(), a.getId(), a.getName(), format.format(b.getStartDate()), format.format(b.getEndDate()), b.getBookingStatus().toString(),
                                    b.getPaymentStatus().toString(), b.getGuest().getName(), b.getGuest().getEmail(), b.getGuest().getPhone(), guestAlias, guestQualification));
                }

            }
        } else {
            // -- Si no hay filtros de fecha ni de estado de reserva, hay que cargar todas las reservas de los alojamientos válidos.
            for (Accommodation a : accommodations) {
                List<Booking> bookings = bookingRepository.findBookingsByAccommodation_Id(a.getId());
                for (Booking b: bookings){
                    String  guestAlias = b.getGuest().getAlias();
                    Integer guestQualification = userCalificationService.getUserQualificationByQualifyingAndQualifiedUsers(alias, guestAlias);
                    responses.add(new HostBookingsResponse(b.getId(), a.getId(), a.getName(), format.format(b.getStartDate()), format.format(b.getEndDate()), b.getBookingStatus().toString(),
                            b.getPaymentStatus().toString(), b.getGuest().getName(), b.getGuest().getEmail(), b.getGuest().getPhone(),guestAlias, guestQualification));
                }
            }
        }

        Pageable pageable = PageRequest.of(nroPag, cantReg);
        Page<HostBookingsResponse> listResultPage = listConvertToPage1(responses, pageable);

        return new HostBookingsListResponse(listResultPage.getContent(), listResultPage.getNumber(), listResultPage.getSize(),
                listResultPage.getTotalElements(), listResultPage.getTotalPages(), listResultPage.isLast());
    }

    @Override
    public List<GuestBookingsInfoResponse> getGuestBookings(String alias) {
        List<Booking> bookings = bookingRepository.guestBookingsStartDateOrderDesc(alias);
        List<GuestBookingsInfoResponse> responses = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        for (Booking b: bookings){
            Set<Gallery> fotos = b.getAccommodation().getGallery();
            String firstPhoto = null;
            for (Gallery f: fotos){
                if (f.getOrderPhoto() == 1){
                    firstPhoto = f.getName()+"/"+f.getPhoto();
                    break;
                }
            }
            responses.add(new GuestBookingsInfoResponse(b.getId(), b.getAccommodation().getId(), b.getAccommodation().getName(), format.format(b.getStartDate()), format.format(b.getEndDate()),
                                                        b.getBookingStatus().toString(), b.getPaymentStatus().toString(), b.getAccommodation().getHost().getName(),
                                                        b.getAccommodation().getHost().getEmail(),firstPhoto));
        }
        return responses;
    }

    @Override
    public GuestBookingDetailResponse getBookingDetailByBookingId(int id) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Booking booking = bookingRepository.getById(id);
        int reviewId = 0;
        int reviewQual = 0;
        String reviewDesc = null;
        if (booking.getReview() != null){
            reviewId = booking.getReview().getId();
            reviewQual = booking.getReview().getQualification();
            reviewDesc = booking.getReview().getDescription();
        }

        Set<Gallery> fotos = booking.getAccommodation().getGallery();
        String firstPhoto = null;
        for (Gallery f: fotos){
            if (f.getOrderPhoto() == 1){
                firstPhoto = f.getName()+"/"+f.getPhoto();
                break;
            }
        }

        String hostAlias = booking.getAccommodation().getHost().getAlias();
        Integer hostQualification = userCalificationService.getUserQualificationByQualifyingAndQualifiedUsers(booking.getGuest().getAlias(), hostAlias);

        return new GuestBookingDetailResponse(booking.getId(), booking.getAccommodation().getId(), booking.getAccommodation().getName(),
                format.format(booking.getStartDate()), format.format(booking.getEndDate()), booking.getBookingStatus().toString(), booking.getPaymentStatus().toString(),
                booking.getAccommodation().getHost().getName(), booking.getAccommodation().getHost().getEmail(), booking.getFinalPrice(), reviewId, reviewDesc, reviewQual, firstPhoto,
                hostAlias, hostQualification);
    }

    @Override
    public void addBookingReview(AddReviewDTO reviewDTO) {
        Booking booking = bookingRepository.getById(reviewDTO.getBookingId());
        Review newReview = reviewService.addReview(reviewDTO.getDescription(), reviewDTO.getQualification());
        booking.setReview(newReview);
        bookingRepository.save(booking);
    }

    @Override
    public List<GuestBookingInfoDetailResponse> getGuestBookingsWithAllInfo(String alias) {
        List<Booking> bookings = bookingRepository.guestBookingsStartDateOrderDesc(alias);
        List<GuestBookingInfoDetailResponse> responses = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        int reviewId = 0;
        int reviewQual = 0;
        String reviewDesc = null;
        for (Booking b: bookings){
            reviewId = 0;   reviewQual = 0;     reviewDesc = null;
            if (b.getReview() != null){
                reviewId = b.getReview().getId();
                reviewQual = b.getReview().getQualification();
                reviewDesc = b.getReview().getDescription();
            }
            String hostAlias = b.getAccommodation().getHost().getAlias();
            Integer hostQualification = userCalificationService.getUserQualificationByQualifyingAndQualifiedUsers(b.getGuest().getAlias(), hostAlias);

            responses.add(new GuestBookingInfoDetailResponse(b.getId(), b.getAccommodation().getId(), b.getAccommodation().getName(), format.format(b.getStartDate()), format.format(b.getEndDate()),
                    b.getBookingStatus().toString(), b.getPaymentStatus().toString(), b.getAccommodation().getHost().getName(), b.getAccommodation().getHost().getEmail(), b.getFinalPrice(),
                    reviewId, reviewDesc, reviewQual, hostAlias, hostQualification));
        }
        return responses;
    }

    @Override
    public String getAliasGuestByBookingId(int id) {
        Booking booking = bookingRepository.getById(id);
        return booking.getGuest().getAlias();
    }

    @Override
    public List<ReviewResponse> getReviewsByAccommodationId(int id) {
        List<Booking> bookings = bookingRepository.findBookingsByAccommodation_Id(id);
        List<ReviewResponse> reviewResponses = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        for (Booking b: bookings){
            if (b.getReview() != null){
                reviewResponses.add(new ReviewResponse(b.getGuest().getAlias(), b.getGuest().getName(), b.getGuest().getPicture(),
                                                        b.getReview().getQualification(), b.getReview().getDescription(), format.format(b.getReview().getCreatedAt())));
            }
        }
        return reviewResponses;
    }
}
