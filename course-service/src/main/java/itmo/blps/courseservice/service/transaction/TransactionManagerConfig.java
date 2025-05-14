package itmo.blps.courseservice.service.transaction;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import jakarta.transaction.UserTransaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
public class TransactionManagerConfig {

    @Bean(initMethod = "init", destroyMethod = "close")
    public UserTransactionManager atomikosTransactionManager() {
        UserTransactionManager transactionManager = new UserTransactionManager();
        transactionManager.setForceShutdown(true);
        return transactionManager;
    }

    @Bean
    public UserTransaction userTransaction() {
        return new UserTransactionImp();
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            UserTransactionManager atomikosTransactionManager,
            UserTransaction userTransaction
    ) {
        return new JtaTransactionManager(userTransaction, atomikosTransactionManager);
    }
}
