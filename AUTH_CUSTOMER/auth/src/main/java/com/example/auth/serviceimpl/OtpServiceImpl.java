package com.example.auth.serviceimpl;

import com.example.auth.service.OtpService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpServiceImpl implements OtpService {

    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();

    private final Random random = new Random();

    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final int MAX_ATTEMPTS = 5;

    @Override
    public void generateAndSendOtp(String identifier) {

        String otp = String.format("%06d", random.nextInt(1_000_000));
        //basically what it means is- %d means formatting of an integer
        // it checks whether the length of the number is 6, if yes then it leaves as it is. If NO then fill 0s at the start
        // 342->000342

        OtpData otpData = new OtpData(
                otp,
                LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES),
                0
        );

        otpStore.put(identifier, otpData);

        // OTP simulation
        System.out.println("=================================");
        System.out.println("OTP SIMULATION");
        System.out.println("Recipient: " + identifier);
        System.out.println("OTP: " + otp);
        System.out.println("Expires in: " + OTP_EXPIRY_MINUTES + " minutes");
        System.out.println("=================================");
    }

    @Override
    public boolean verifyOtp(String identifier, String enteredOtp) {

        OtpData otpData = otpStore.get(identifier);

        if (otpData == null) {
            return false;
        }

        // instead of having a counter running which will delete the otp once time gets over we can do this
        // while verification just check if the time expired which is must simpler and less work
        if (LocalDateTime.now().isAfter(otpData.expiryTime())) {
            otpStore.remove(identifier);
            return false;
        }

        if (otpData.attempts() >= MAX_ATTEMPTS) {
            otpStore.remove(identifier);
            return false;
        }

        if (!otpData.otp().equals(enteredOtp)) {

            otpStore.put(
                identifier,
                new OtpData(
                    otpData.otp(),
                    otpData.expiryTime(),
                    otpData.attempts() + 1
                )
            );

            return false;
        }

        // OTP can only be used once
        otpStore.remove(identifier);

        return true;
    }

    private record OtpData(
            String otp,
            LocalDateTime expiryTime,
            int attempts
    ) {
    }
}