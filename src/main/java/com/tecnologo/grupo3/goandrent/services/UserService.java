package com.tecnologo.grupo3.goandrent.services;

import com.tecnologo.grupo3.goandrent.dtos.InfoProfileDTO;
import com.tecnologo.grupo3.goandrent.dtos.QualifyUserDTO;
import com.tecnologo.grupo3.goandrent.dtos.UpdatePasswordDTO;
import com.tecnologo.grupo3.goandrent.dtos.UserInformationDTO;
import com.tecnologo.grupo3.goandrent.entities.User;
import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface UserService {
    Boolean existUserByAliasOrEmail(String alias, String email);
    void recoverPassword(int code, Date startDate, Date endDate, String email);
    Boolean isValidCode(String email, int code);
    void changePassword(String email, String password);
    List<UserInformationDTO> getUsers();
    String getUserRoleByEmail(String email);
    String getUserRoleByAlias(String alias);
    void updateUserStatus(String alias, UserStatus status);
    void notifyStatusAccount(String alias, UserStatus status);
    InfoProfileDTO getInfoProfile(String alias);
    void updateUserInformation(InfoProfileDTO infoProfileDTO) throws ParseException;
    Boolean isValidUser(String email);
    String getUserName(String email);
    String getUserAlias(String email);
    void updatePasswordProfile(UpdatePasswordDTO updatePasswordDTO);
    User getUserByAlias(String alias);
    void qualifyUser(QualifyUserDTO userDTO);
    void deleteGuestQualification(String hostAlias, String guestAlias);
    void qualifyUserHost(QualifyUserDTO userDTO);
    void deleteHostQualification(String hostAlias, String guestAlias);
}
