package itmo.blps.elearningplatform.service.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class TransactionalAspect {

    private final PlatformTransactionManager transactionManager;

    @Around("@annotation(transactional)")
    public Object manageTransaction(ProceedingJoinPoint joinPoint, Transactional transactional) throws Throwable {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(transactional.propagation().value());
        def.setIsolationLevel(transactional.isolation().value());

        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            log.atInfo()
                    .setMessage("Starting transaction")
                    .addKeyValue("joinPoint", joinPoint.getSignature().getName())
                    .addKeyValue("propagation", transactional.propagation())
                    .addKeyValue("isolation", transactional.isolation())
                    .log();
            Object result = joinPoint.proceed();
            transactionManager.commit(status);
            log.atInfo()
                    .setMessage("Transaction committed successfully")
                    .addKeyValue("joinPoint", joinPoint.getSignature().getName())
                    .log();
            return result;
        } catch (Exception e) {
            log.atError()
                    .setMessage("Transaction failed, rolling back")
                    .addKeyValue("joinPoint", joinPoint.getSignature().getName())
                    .log();
            transactionManager.rollback(status);
            throw e;
        }
    }
}

