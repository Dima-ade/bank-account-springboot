package ro.adela.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.adela.bank.BankAccountDto;
import ro.adela.bank.BankDataDto;

@Repository
public interface BankRepository extends JpaRepository<BankAccountDto, Integer> {
}
