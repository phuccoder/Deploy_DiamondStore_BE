package com.example.diamondstore.service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.diamondstore.model.Account;
import com.example.diamondstore.model.AccumulatePoints;
import com.example.diamondstore.repository.AccountRepository;
import com.example.diamondstore.repository.AccumulatePointsRepository;
import com.example.diamondstore.request.AccumulatePointsRequest;

@Service
public class AccumulatePointsService {

    @Autowired
    private AccumulatePointsRepository accumulatePointsRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public boolean deductPoints(int AccountID, int point) {
        Optional<AccumulatePoints> optionalAccumulatePoints = accumulatePointsRepository.findById(AccountID);
        if (optionalAccumulatePoints.isPresent()) {
            AccumulatePoints accumulatePoints = optionalAccumulatePoints.get();
            if (accumulatePoints.getPoint() >= point) {
                accumulatePoints.setPoint(accumulatePoints.getPoint() - point);
                accumulatePointsRepository.save(accumulatePoints);
                return true;
            }
        }
        return false;
    }

    public long getTotalAccumulatePoints() {
        return accumulatePointsRepository.count();
    }

    public Iterable<AccumulatePoints> getAllAccumulatePointss() {
        return accumulatePointsRepository.findAll();
    }

    public Optional<AccumulatePoints> getAccumulatePointsById(Integer accountID) {
        return accumulatePointsRepository.findById(accountID);
    }

    @Transactional
    public boolean updateAccumulatePoints(Integer accountID, AccumulatePointsRequest updatedAccumulatePointsRequest) {
        Optional<Account> optionalAccount = accountRepository.findById(accountID);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setAccountName(updatedAccumulatePointsRequest.getAccountName());
            account.setPassword(updatedAccumulatePointsRequest.getPassword());
            account.setPhoneNumber(updatedAccumulatePointsRequest.getPhoneNumber());
            accountRepository.save(account);

            AccumulatePoints accumulatePoints = account.getAccumulatePoints();
            if (accumulatePoints != null) {
                accumulatePoints.setPoint(updatedAccumulatePointsRequest.getPoint());
                accumulatePointsRepository.save(accumulatePoints);
            }
            return true;
        }
        return false;
    }

    @Transactional
    public ResponseEntity<Map<String, String>> deleteAccumulatePoints(Integer accountID) {
        Optional<Account> optionalAccount = accountRepository.findById(accountID);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            AccumulatePoints accumulatePoints = account.getAccumulatePoints();
            if (accumulatePoints != null) {
                accumulatePointsRepository.delete(accumulatePoints);
            }
            accountRepository.delete(account);
            return ResponseEntity.ok(Collections.singletonMap("message", "Xóa thành công"));
        }
        return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Không tìm thấy tài khoản"));
    }
}
