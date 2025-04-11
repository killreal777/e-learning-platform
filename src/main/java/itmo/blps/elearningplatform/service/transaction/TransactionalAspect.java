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
            log.info(
                    "Starting transaction with propagation: {}, isolation: {}",
                    transactional.propagation(), transactional.isolation()
            );
            Object result = joinPoint.proceed();
            transactionManager.commit(status);
            log.info("Transaction committed successfully");
            return result;
        } catch (Exception e) {
            log.error("Transaction failed, rolling back", e);
            transactionManager.rollback(status);
            throw e;
        }
    }
}

