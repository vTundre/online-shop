package security.service;

public interface PasswordService {

    String getSaltedHash(String password);

    boolean check(String password, String stored);
}
