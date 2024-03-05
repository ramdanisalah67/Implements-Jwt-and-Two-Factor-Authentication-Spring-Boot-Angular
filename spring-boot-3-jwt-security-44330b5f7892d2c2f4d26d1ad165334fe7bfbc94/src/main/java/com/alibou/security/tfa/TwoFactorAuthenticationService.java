package com.alibou.security.tfa;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import dev.samstevens.totp.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TwoFactorAuthenticationService {

    public String generateNewSecret(){
        return new  DefaultSecretGenerator().generate();
    }

    public String generateQrCodeImageUri(String secret){
        QrData data = new QrData.Builder()
                .label("salah-eddine 2FA example")
                .secret(secret)
                .issuer("Ramdani salah-eddine")
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();

        QrGenerator generator = new ZxingPngQrGenerator();
        byte[] imageDate = new byte[0];
        try {
            imageDate=generator.generate(data);
        }
         catch (QrGenerationException e) {
            e.printStackTrace();
            log.error("Error While generating Qr-Code");
        }

        return Utils.getDataUriForImage(imageDate,generator.getImageMimeType());

    }

    public  boolean isOtpValid(String secret,String code){
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier codeVerifier= new DefaultCodeVerifier(codeGenerator,timeProvider);
        return codeVerifier.isValidCode(secret,code);
    }

    public boolean isOtpNotValid(String secret,String code){
        return !this.isOtpValid(secret,code);
    }
}
