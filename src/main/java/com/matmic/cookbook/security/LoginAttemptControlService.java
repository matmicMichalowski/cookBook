package com.matmic.cookbook.security;



import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptControlService {

    private final int MAX_FAIL_ATTEMPT = 5;
    private LoadingCache<String, Integer> attemptsCache;

    public LoginAttemptControlService(){
        super();
        attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
                    public Integer load(String key){
                        return 0;
                    }
                });
    }

    public void loginSucceeded(String key){
        attemptsCache.invalidate(key);
    }

    public void loginFailed(String key){
        int attempts;
        try{
            attempts = attemptsCache.get(key);
        }catch (ExecutionException ex){
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked(String key){
        try{
            return attemptsCache.get(key) >= MAX_FAIL_ATTEMPT;
        }catch (ExecutionException ex){
            return false;
        }
    }
}
