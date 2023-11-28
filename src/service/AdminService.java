package service;



import java.util.List;
import java.util.Optional;
import models.Transaction;
import models.User;
import reposiroty.DataRepository;
import reposiroty.UserRepository;

public class AdminService {

  private final UserRepository userRepository;
  private final DataRepository dataRepository;

  public AdminService(UserRepository userRepository, DataRepository dataRepository) {
    this.userRepository = userRepository;
    this.dataRepository = dataRepository;
  }

//  public boolean changeCurrencyExchange (int typeOfExchange, double amount) {
//    //TODO switchCASE 1/2/3 на каждом типе будет своя валюта
//    dataRepository.changeTheRate(currency, amount);
//    return true;
//  }

  //TODO
//  public boolean addTheCurrency() {
//    return true;
//  }
  //TODO
//  public boolean DeleteTheCurrency() {
//    return true;
//  }
//  public Optional<Transaction> showTransactionsOfUser(int id) {
//    List<Transaction> transactions = dataRepository.showAllTransactions(id);
//    //TODO
//  }


}
