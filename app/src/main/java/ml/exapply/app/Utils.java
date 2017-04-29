package ml.exapply.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public  class Utils {
	public static boolean isNetworkConnected(Context context) {
		  ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		  NetworkInfo ni = cm.getActiveNetworkInfo();
		  if (ni == null) {
		   // There are no active networks.
		   return false;
		  } else
		   return true;
		 }
	public static boolean validatePhoneNumber(String mPhoneNumber){
		String expression = "^[0-9-1+]{8}$";
		CharSequence inputStr = mPhoneNumber;
		Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		return (matcher.matches())? true : false;
	}
	public static String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		}
		return capitalize(manufacturer) + " " + model;
	}

	private static String capitalize(String str) {
		if (TextUtils.isEmpty(str)) {
			return str;
		}
		char[] arr = str.toCharArray();
		boolean capitalizeNext = true;
		String phrase = "";
		for (char c : arr) {
			if (capitalizeNext && Character.isLetter(c)) {
				phrase += Character.toUpperCase(c);
				capitalizeNext = false;
				continue;
			} else if (Character.isWhitespace(c)) {
				capitalizeNext = true;
			}
			phrase += c;
		}
		return phrase;
	}
	public static boolean CheckEmailAddress(String email){
		boolean isAvailable= false;
		String regEmail = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
		Pattern emailPattern = Pattern.compile(regEmail);
		Matcher emailMatcher = emailPattern.matcher(email);
		isAvailable = emailMatcher.matches();

		return isAvailable;
	}
	public static boolean VerifyCivilId(String civilId)
	{
		if (civilId.trim().equalsIgnoreCase("")){
			return false;
		}else {
			boolean output = false;
			long outLongValue = Long.parseLong(civilId);
			if (civilId.length() == 12) {
				int c1 = Integer.parseInt(civilId.substring(0, 1));
				int c2 = Integer.parseInt(civilId.substring(1, 2));
				int c3 = Integer.parseInt(civilId.substring(2, 3));
				int c4 = Integer.parseInt(civilId.substring(3, 4));
				int c5 = Integer.parseInt(civilId.substring(4, 5));
				int c6 = Integer.parseInt(civilId.substring(5, 6));
				int c7 = Integer.parseInt(civilId.substring(6, 7));
				int c8 = Integer.parseInt(civilId.substring(7, 8));
				int c9 = Integer.parseInt(civilId.substring(8, 9));
				int c10 = Integer.parseInt(civilId.substring(9, 10));
				int c11 = Integer.parseInt(civilId.substring(10, 11));
				int total = 11 - (((c1 * 2) + (c2 * 1) + (c3 * 6) + (c4 * 3) + (c5 * 7) +
						(c6 * 9) + (c7 * 10) + (c8 * 5) + (c9 * 8) + (c10 * 4) + (c11 * 2)) % 11);
				int c12 = Integer.parseInt(civilId.substring(11, 12));
				if (c12 == total) {
					output = true;
				} else {
					output = false;
				}

			}

			return output;
		}
	}
}
