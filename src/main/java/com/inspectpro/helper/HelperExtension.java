package com.inspectpro.helper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class HelperExtension {

    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    // private static LoadingCache<String, String> otpCache;

    // static {
    //     otpCache = CacheBuilder.newBuilder()
    //         .expireAfterWrite(10, TimeUnit.MINUTES)
    //         .build(new CacheLoader<String, String>() {
    //             @Override
    //             public String load(String key) {
    //                 return "";
    //             }
    //         });
    // }
	
	// public static void saveUserOtp(String userName, String otp) {
	// 	otpCache.put(userName, otp);
	// }
	
	// public static String getUserOtp(String userName) {
	// 	try {
	// 		return otpCache.get(userName);
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 		return null;
	// 	}
	// }
	
	// public static boolean isValidEmail(String email) {
    //     try {
    //         InternetAddress address = new InternetAddress(email);
    //         address.validate();
    //         return true;
    //     } catch (Exception e) {
    //         return false;
    //     }
    // }

    public static boolean checkLocalEnvironment() {
	    try {
	        String hostname = InetAddress.getLocalHost().getHostName();
	        if ("localhost".equalsIgnoreCase(hostname) || hostname.contains("local")) {
	            return true;
	        } else {
	            return false;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public static boolean isValidMobileNumber(String mobile) {
	    return mobile != null && mobile.matches("^[6-9]\\d{9}$");
	}
	
	public static boolean isValidInternationalMobile(String mobile) {
	    return mobile != null && mobile.matches("^\\+[1-9]\\d{1,14}$");
	}
	
	public static String escapeSpecialCharacters(String search) {
		return search.replaceAll("'{2,}", "'").replaceAll("([\\W&&[^_]])", "\\\\$1");
	}

	public static String generateUniqueAlphaNumericId(String prefix, int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }
        return prefix + LocalDateTime.now() + sb.toString();
    }
	
	public static String getSixDigitOtp() {
		String otp = "";
		SecureRandom rand = new SecureRandom();
		for (int i = 0; i < 6; i++) {
			int n = rand.nextInt(9);
			if (i == 0 && n == 0) {
				n = 1;
			}
			otp = otp.concat(String.valueOf(n));
		}
		return "123456";
	}

	public static boolean isNullOrEmpty(Object message) {
		if (message != null) {
			if (!message.toString().isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isNullOrEmptyOrEmptyString(Object message) {
		if (message != null) {
			if (!message.toString().isEmpty() && !message.toString().equalsIgnoreCase("null")) {
				return false;
			}
		}
		return true;
	}

	public static String getMd5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger no = new BigInteger(1, messageDigest);
			String hashtext = no.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getUserFullName(String firstName, String midName, String lastName) {
	    StringBuilder fullName = new StringBuilder();
	    
	    if (firstName != null && !firstName.trim().isEmpty()) {
	        fullName.append(firstName.trim());
	    }
	    if (midName != null && !midName.trim().isEmpty()) {
	        if (fullName.length() > 0) fullName.append(" ");
	        fullName.append(midName.trim());
	    }
	    if (lastName != null && !lastName.trim().isEmpty()) {
	        if (fullName.length() > 0) fullName.append(" ");
	        fullName.append(lastName.trim());
	    }
	    
	    return fullName.toString();
	}
	
	 public static BigDecimal calculatePercentage(BigDecimal worked, BigDecimal totalWork) {
	        if (totalWork == null || totalWork.compareTo(BigDecimal.ZERO) == 0) {
	            return BigDecimal.ZERO; // avoid division by zero
	        }

	        return worked
	                .divide(totalWork, 4, RoundingMode.HALF_UP) // scale to 4 for precision
	                .multiply(BigDecimal.valueOf(100))
	                .setScale(2, RoundingMode.HALF_UP); // final result with 2 decimal points
	}

}
