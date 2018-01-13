package com.matmic.cookbook.controller.viewmodel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetTokenAndPasswordVM {

    private String resetToken;
    private String newPassword;
}
