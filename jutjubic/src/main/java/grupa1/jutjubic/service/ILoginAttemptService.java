package grupa1.jutjubic.service;

public interface ILoginAttemptService {
    void loginSucceeded(String key);
    boolean tryAttempt(String key);
}
