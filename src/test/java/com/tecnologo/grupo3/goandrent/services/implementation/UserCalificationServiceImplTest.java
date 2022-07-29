package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.entities.User;
import com.tecnologo.grupo3.goandrent.entities.UserCalification;
import com.tecnologo.grupo3.goandrent.entities.ids.UserCalificationID;
import com.tecnologo.grupo3.goandrent.exceptions.UserException;
import com.tecnologo.grupo3.goandrent.repositories.UserCalificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class UserCalificationServiceImplTest {

    @Mock
    private UserCalificationRepository userCalificationRepository;

    @InjectMocks
    private UserCalificationServiceImpl userCalificationService;



    private Optional<Float> qualification;
    private UserCalificationID userCalificationID;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        qualification = Optional.of(Float.parseFloat("10"));
        userCalificationID = new UserCalificationID("user", new User());
    }

    @Test
    void getUserQualification() {
        when(userCalificationRepository.getAverageQualification("alias")).thenReturn(qualification);
        assertNotNull(userCalificationService.getUserQualification("alias"));
    }

    @Test
    void addUserQualification() {
        when(userCalificationRepository.save(new UserCalification(userCalificationID, 4, new User()))).thenReturn(new UserCalification(userCalificationID, 4, new User()));
        userCalificationService.addUserQualification("alias", "user", 4, new User(), new User());
    }

    @Test
    void deleteQualification() {
        when(userCalificationRepository.findUserCalificationByUserCalificationID_QualifiedUserAndUserCalificationID_QualifyingUser(new User(), "alias"))
                .thenReturn(Optional.of(new UserCalification(userCalificationID, 4, new User())));
        userCalificationService.deleteQualification("alias", new User());
    }

    @Test
    void deleteQualification_notFound() {
        when(userCalificationRepository.findUserCalificationByUserCalificationID_QualifiedUserAndUserCalificationID_QualifyingUser(new User(), "alias"))
                .thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userCalificationService.deleteQualification("alias", new User()));
        }

    @Test
    void getUserQualificationByQualifyingAndQualifiedUsers() {
        when(userCalificationRepository.getUserCalificationByQualifyingAndQualifiedUsers("qualifyingUser", "qualifiedUser")).thenReturn(Optional.of(1));
        assertNotNull(userCalificationService.getUserQualificationByQualifyingAndQualifiedUsers("qualifyingUser", "qualifiedUser"));
    }
}