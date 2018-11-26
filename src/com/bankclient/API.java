package com.bankclient;

import com.bankclient.model.Bank;
import com.bankclient.model.Customer;
import com.bankclient.model.Operation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class API {

    private Scanner in;
    private PrintWriter out;
    private Socket s;

    public API(int port) throws IOException {

        s = new Socket("localhost", port);

        InputStream instream = s.getInputStream();
        OutputStream outstream = s.getOutputStream();

        in = new Scanner(instream);
        out = new PrintWriter(outstream);
    }

    public ArrayList<Bank> getBanks(){

        ArrayList<Bank> banks = new ArrayList<>();

        System.out.println("BANKS");
        out.println("BANKS");
        out.flush();

        String response = in.nextLine();

        System.out.println(response);
        int count = Integer.parseInt(response);

        for (int i = 0; i < count; i++){

            int id = in.nextInt();
            String name = in.next();

            while (!in.hasNextInt())
                name = name + " " + in.next();

            int port = in.nextInt();

            System.out.println(name);
            Bank bank = new Bank();
            bank.setId(id);
            bank.setName(name);
            bank.setPort(port);

            banks.add(bank);
        }

        return banks;
    }

    public String registerCustomer(int bankID, String name, double amount , String password){

        System.out.println("REGISTER " + bankID + " " + name + " " + amount + " " + password);
        out.println("REGISTER " + bankID + " " + name + " " + amount + " " + password);
        out.flush();

        String response = in.nextLine() + "\n" + in.nextLine();
        System.out.println(response);

        return response;

    }

    public Customer loginCustomer(int accNo, String password){

        System.out.println("LOGIN " + accNo + " " + password);
        out.println("LOGIN " + accNo + " " + password);
        out.flush();

        String response = in.nextLine() + "\n" + in.nextLine();
        System.out.println(response);

        if (response.contains("Success")){
            int bankID = in.nextInt();

            String bankName = in.next();
            String name = "";

            if (!in.hasNextInt())
                bankName += in.next();

            int bankPort = in.nextInt();

            if (!in.hasNextInt())
                name += in.next();


            Customer customer = new Customer();

            Bank bank = new Bank();
            bank.setId(bankID);
            bank.setName(bankName);
            bank.setPort(bankPort);

            customer.setBank(bank);
            customer.setName(name);
            customer.setAccountNumber(String.valueOf(accNo));

            return customer;
        }

        return null;

    }

    public Customer checkCustomer(int accNo, int bankID, int sameFlag){

        System.out.println("CUSTOMER " + accNo + " " + bankID + " " + sameFlag);
        out.println("CUSTOMER " + accNo + " " + bankID + " " + sameFlag);
        out.flush();

        String response = in.nextLine();
        System.out.println(response);

        if (response.contains("Success")){

            String name = in.nextLine();

            Customer customer = new Customer();

            Bank bank = new Bank();
            bank.setId(bankID);
            customer.setBank(bank);
            customer.setName(name);
            customer.setAccountNumber(String.valueOf(accNo));

            return customer;
        }
        else {
            String error = in.nextLine();
        }

        return null;

    }

    public double balance(){

        System.out.println("BALANCE");
        out.println("BALANCE");
        out.flush();

        String response = in.nextLine();
        System.out.println(response);

        if (!response.isEmpty())
            return Double.parseDouble(response);

        return 0;

    }

    public String makeDeposit(double amount){

        System.out.println("DEPOSIT " + amount + " 0 " + Session.accountNumber());
        out.println("DEPOSIT " + amount + " 0 " + Session.accountNumber());
        out.flush();

        String response = in.nextLine() + "\n" + in.nextLine();
        System.out.println(response);

        return response;

    }

    public String makeWithdraw(double amount){

        System.out.println("WITHDRAW " + amount + " 1");
        out.println("WITHDRAW " + amount + " 1");
        out.flush();

        String response = in.nextLine() + "\n" + in.nextLine();
        System.out.println(response);

        return response;

    }

    public String makeTransfer(double amount, int accNo, String bank){

        System.out.println("TRANSFER " + amount + " " + accNo + " " + bank);
        out.println("TRANSFER " + amount + " " + accNo + " " + bank);
        out.flush();

        String response = in.nextLine() + "\n" + in.nextLine();
        System.out.println(response);

        return response;

    }

    public ArrayList<Operation> history(){

        ArrayList<Operation> operations = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        System.out.println("HISTORY");
        out.println("HISTORY");
        out.flush();

        String response = in.nextLine();

        System.out.println(response);
        int count = Integer.parseInt(response);

        for (int i = 0; i < count; i++){

            int type = in.nextInt();
            double amount = in.nextDouble();
            String date = in.next();

            System.out.println(date);
            Operation operation = new Operation();
            operation.setType(type);
            operation.setAmount(amount);

            try {
                operation.setDate(simpleDateFormat.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            operations.add(operation);
        }

        return operations;
    }
}
