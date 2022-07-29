package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.InfoProfileDTO;
import com.tecnologo.grupo3.goandrent.dtos.QualifyUserDTO;
import com.tecnologo.grupo3.goandrent.dtos.UpdatePasswordDTO;
import com.tecnologo.grupo3.goandrent.dtos.UserInformationDTO;
import com.tecnologo.grupo3.goandrent.entities.Accommodation;
import com.tecnologo.grupo3.goandrent.entities.User;
import com.tecnologo.grupo3.goandrent.entities.UserCalification;
import com.tecnologo.grupo3.goandrent.entities.ids.UserCalificationID;
import com.tecnologo.grupo3.goandrent.entities.users_types.Host;
import com.tecnologo.grupo3.goandrent.exceptions.UserException;
import com.tecnologo.grupo3.goandrent.repositories.UserRepository;
import com.tecnologo.grupo3.goandrent.services.*;
import com.tecnologo.grupo3.goandrent.services.email.EMailService;
import com.tecnologo.grupo3.goandrent.utils.enums.Bank;
import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.DiscriminatorValue;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HostService hostService;

    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RecoveryPasswordService recoveryPasswordService;

    @Autowired
    private UserCalificationService userCalificationService;

    @Autowired
    private EMailService eMailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Boolean existUserByAliasOrEmail(String alias, String email) {
        return userRepository.existsUserByAliasOrEmail(alias, email);
    }

    @Override
    public void recoverPassword(int code, Date startDate, Date endDate, String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "Usuario no encontrado con el correo: " + email));
        if (user.getUserStatus() == UserStatus.ACEPTADO) {
            recoveryPasswordService.saveNewRecoveryPassword(code, startDate, endDate, user);
            eMailService.sendEmailRecoveryPassword(email, user.getName(), code);
        } else throw new UserException(HttpStatus.BAD_REQUEST, "No se puede cambiar la contraseña de este usuario");
    }

    @Override
    public Boolean isValidCode(String email, int code) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "Usuario no encontrado con el correo: " + email));

        return recoveryPasswordService.isValidRecoveryCode(code, user);
    }

    @Override
    public void changePassword(String email, String password) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "Usuario no encontrado con el correo: " + email));;
        if (user.getUserStatus() == UserStatus.ACEPTADO) {
            user.setPassword(password);
            userRepository.save(user);
        } else throw new UserException(HttpStatus.BAD_REQUEST, "No se puede cambiar la contraseña de este usuario");
    }

    @Override
    public List<UserInformationDTO> getUsers() {
        List<User> allUsers = userRepository.findAll();
        List<UserInformationDTO> allUsers_response = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormatSymbols simbols = new DecimalFormatSymbols();
        simbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("####.##", simbols);
        UserInformationDTO user = null;
        for (User u: allUsers){
            String created_date = format.format(u.getCreatedAt());
            String role = u.getClass().getAnnotation(DiscriminatorValue.class).value();
            user = new UserInformationDTO(u.getAlias(), u.getEmail(), u.getName(), u.getLastName(), created_date,
                                            u.getUserStatus().toString(), u.getPhone(), role);

            if (role.equals("ROLE_HOST") || role.equals("ROLE_GUEST")){
                user.setRating(Float.parseFloat(decimalFormat.format(userCalificationService.getUserQualification(u.getAlias()).floatValue())));
                if (role.equals("ROLE_HOST")) {
                    List<Accommodation> accommodations = accommodationService.getAccommodationsByHostAlias(u.getAlias());
                    if (!accommodations.isEmpty() && accommodations.size() > 0) {
                        Accommodation acc = accommodations.get(0);
                        user.setAccommodationId(acc.getId());
                    }
                }
            }
            allUsers_response.add(user);
        }
        return allUsers_response;
    }

    @Override
    public String getUserRoleByEmail(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "Usuario no encontrado con el correo: " + email));
        return user.getClass().getAnnotation(DiscriminatorValue.class).value();
    }

    @Override
    public String getUserRoleByAlias(String alias) {
        User user = userRepository.findUserByAlias(alias)
                .orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "Usuario no encontrado con el alias: " + alias));
        return user.getClass().getAnnotation(DiscriminatorValue.class).value();
    }

    @Override
    public void updateUserStatus(String alias, UserStatus status) {
        User user = userRepository.findUserByAlias(alias)
                .orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "Usuario no encontrado con el alias: " + alias));
        user.setUserStatus(status);
        userRepository.save(user);
    }

    @Override
    public void notifyStatusAccount(String alias, UserStatus status) {
        User user = userRepository.findUserByAlias(alias).get();
        String name = user.getName();
        String email = user.getEmail();
        eMailService.sendEmailStatusAccount(email, name, status);
    }

    @Override
    public InfoProfileDTO getInfoProfile(String alias) {
        DecimalFormatSymbols simbols = new DecimalFormatSymbols();
        simbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("####.##", simbols);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        User user = userRepository.findUserByAlias(alias)
                .orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "Usuario no encontrado con el alias: " + alias));;
        String birthday = format.format(user.getBirthday());
        InfoProfileDTO infoProfileDTO = new InfoProfileDTO(user.getAlias(), user.getEmail(), user.getName(), user.getLastName(),
                                                            user.getPhone(), birthday, user.getPicture());
        String role = user.getClass().getAnnotation(DiscriminatorValue.class).value();
        if (role.equals("ROLE_HOST")) {
            Host host = hostService.getHostByEmail(user.getEmail()).orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "No existe un anfitrión con el email: " + user.getEmail()));;
            infoProfileDTO.setAccount(host.getAccount());
            infoProfileDTO.setBank(host.getBank().toString());
        }
        if (role.equals("ROLE_HOST") || role.equals("ROLE_GUEST")){
            infoProfileDTO.setQualification(Float.parseFloat(decimalFormat.format(userCalificationService.getUserQualification(user.getAlias()).floatValue())));
        }

        return infoProfileDTO;
    }

    @Override
    public void updateUserInformation(InfoProfileDTO infoProfileDTO) throws ParseException {
        // -- Los posibles datos para modificar de un usuario son: Nombre, Apellido, Teléfono, fecha de nacimiento y foto.
        User user = userRepository.findUserByAlias(infoProfileDTO.getAlias())
                .orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "Usuario no encontrado con el alias: " + infoProfileDTO.getAlias()));
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date birthday = format.parse(infoProfileDTO.getBirthday());
        user.setBirthday(birthday);
        user.setName(infoProfileDTO.getName());
        user.setLastName(infoProfileDTO.getLastName());
        user.setPhone(infoProfileDTO.getPhone());
        user.setPicture(infoProfileDTO.getPicture());
        String roleUser = user.getClass().getAnnotation(DiscriminatorValue.class).value();
        userRepository.save(user);
        // -- En caso de que el usuario sea anfitrión, podrá modificar además, el banco y la cuenta.
        if (roleUser.equals("ROLE_HOST")){
            Host host = hostService.getHostByEmail(infoProfileDTO.getEmail())
                    .orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "No existe un anfitrión con el email: " + infoProfileDTO.getEmail()));
            host.setAccount(infoProfileDTO.getAccount());
            host.setBank(Bank.valueOf(infoProfileDTO.getBank()));
            userRepository.save(host);
        }
    }

    @Override
    public Boolean isValidUser(String email) {
        return userRepository.existsUserByEmailAndUserStatus(email, UserStatus.ACEPTADO);
    }

    @Override
    public String getUserName(String email) {
        return userRepository.findUserByEmail(email).get().getName();
    }

    @Override
    public String getUserAlias(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "No existe un usuario con el email: " + email))
                .getAlias();
    }

    @Override
    public void updatePasswordProfile(UpdatePasswordDTO updatePasswordDTO) {
        User user = userRepository.findUserByAlias(updatePasswordDTO.getAlias())
                .orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "No existe un usuario con el alias: " + updatePasswordDTO.getAlias()));

        if (passwordEncoder.matches(updatePasswordDTO.getOldPassword(), user.getPassword())){
            user.setPassword(passwordEncoder.encode(updatePasswordDTO.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new UserException(HttpStatus.BAD_REQUEST, "La contraseña actual no es correcta.");
        }
    }

    @Override
    public User getUserByAlias(String alias) {
        return userRepository.findUserByAlias(alias).orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "No existe un usuario con el alias: " + alias));
    }

    @Override
    public void qualifyUser(QualifyUserDTO userDTO) {
        String role1 = getUserRoleByAlias(userDTO.getQualifyingUser());
        if (role1.equals("ROLE_HOST")){
            String role2 = this.getUserRoleByAlias(userDTO.getQualifiedUser());
            if (role2.equals("ROLE_GUEST")) {
                userCalificationService.addUserQualification(userDTO.getQualifyingUser(), userDTO.getQualifiedUser(), userDTO.getQualification(),
                        getUserByAlias(userDTO.getQualifyingUser()), getUserByAlias(userDTO.getQualifiedUser()));
            } else {
                throw new UserException(HttpStatus.BAD_REQUEST, "El usuario que se está calificando NO es un huésped.");
            }
        } else {
            throw new UserException(HttpStatus.BAD_REQUEST, "El usuario que está calificando NO es anfitrión.");
        }
    }

    @Override
    public void deleteGuestQualification(String hostAlias, String guestAlias) {
        String role = getUserRoleByAlias(hostAlias);
        if (role.equals("ROLE_HOST")){
            role = getUserRoleByAlias(guestAlias);
            if (role.equals("ROLE_GUEST")){
                User qualifiedUser = userRepository.findUserByAlias(guestAlias)
                        .orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "No existe un usuario con el alias: " + guestAlias));
                userCalificationService.deleteQualification(hostAlias, qualifiedUser);
            } else {
                throw new UserException(HttpStatus.BAD_REQUEST, "El usuario al que se le está eliminando la calificación no es huésped.");
            }
        } else {
            throw new UserException(HttpStatus.BAD_REQUEST, "El usuario que está eliminando la calificación no es anfitrión.");
        }
    }

    @Override
    public void qualifyUserHost(QualifyUserDTO userDTO) {
        String role1 = getUserRoleByAlias(userDTO.getQualifyingUser());
        if (role1.equals("ROLE_GUEST")){
            String role2 = getUserRoleByAlias(userDTO.getQualifiedUser());
            if (role2.equals("ROLE_HOST")) {
                userCalificationService.addUserQualification(userDTO.getQualifyingUser(), userDTO.getQualifiedUser(), userDTO.getQualification(),
                        getUserByAlias(userDTO.getQualifyingUser()), getUserByAlias(userDTO.getQualifiedUser()));
            } else {
                throw new UserException(HttpStatus.BAD_REQUEST, "El usuario que se está calificando no es anfitrión.");
            }
        } else {
            throw new UserException(HttpStatus.BAD_REQUEST, "El usuario que está calificando no es un huésped.");
        }
    }

    @Override
    public void deleteHostQualification(String hostAlias, String guestAlias) {
        String role = getUserRoleByAlias(guestAlias);
        if (role.equals("ROLE_GUEST")){
            role = getUserRoleByAlias(hostAlias);
            if (role.equals("ROLE_HOST")){
                User qualifiedUser = userRepository.findUserByAlias(hostAlias)
                        .orElseThrow(() -> new UserException(HttpStatus.NOT_FOUND, "No existe un usuario con el alias: " + hostAlias));
                userCalificationService.deleteQualification(guestAlias, qualifiedUser);
            } else {
                throw new UserException(HttpStatus.BAD_REQUEST, "El usuario al que se le está eliminando la calificación no es anfitrión.");
            }
        } else {
            throw new UserException(HttpStatus.BAD_REQUEST, "El usuario que está eliminando la calificación no es huésped.");
        }
    }
}
