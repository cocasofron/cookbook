package ro.cookbook.util;

import ro.cookbook.domain.User;

import java.util.Set;

public class Utils {

    public static final String IMAGES_ROOT_PATH = "src/main/resources/static/images/";

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (str == null || searchStr == null) return false;

        final int length = searchStr.length();
        if (length == 0)
            return true;

        for (int i = str.length() - length; i >= 0; i--) {
            if (str.regionMatches(true, i, searchStr, 0, length))
                return true;
        }
        return false;
    }

    public static boolean contains(Set<User> users, User user) {
        for (User u : users) {
            if (u.getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }
}
