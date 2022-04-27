package com.company;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class PhoneBookRunner {
    static ArrayList<Contact> phoneBookDataBase = new ArrayList<>();
    static ObjectMapper mapper = new ObjectMapper();
    static SimpleModule module = new SimpleModule();

    public static void main(String[] args) throws IOException {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        module.addSerializer(LocalDate.class, new LocalDateSerializer());
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        mapper.registerModule(module);


        loadContactFromDatabase(mapper);
        System.out.println("Hello in your PhoneBook");
        Scanner generalMenuScanner = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.println("Choose what do you want to do: ");
            System.out.println();
            System.out.println("If you want to watch all contacts, enter 1 ");
            System.out.println("If you want add new contact, enter 2 ");
            System.out.println("If you want to change contact, enter 3 ");
            System.out.println("If you want to sort all contacts, enter 4 ");
            System.out.println("If you want to remove contact, enter 5 ");
            System.out.println("If you want to close this app, enter 6 ");
            System.out.println();
            System.out.println("Enter number of operation");
            String choose = generalMenuScanner.nextLine();

            switch (choose) {
                case "1" -> System.out.println(phoneBookDataBase);
                case "2" -> addNewContact(generalMenuScanner);
                case "3" -> editContact(generalMenuScanner);
                case "4" -> sortContact(generalMenuScanner);
                case "5" -> deleteContact(generalMenuScanner);
                case "6" -> {
                    generalMenuScanner.close();
                    saveInDatabase(mapper);
                    System.exit(0);
                }
                default -> System.out.println("Incorrect choose");
            }
        }
    }

    private static void sortContact(Scanner sortScanner) {
        System.out.println("By which field will we sort?");
        System.out.println();
        System.out.println("Sort by first name - enter 1");
        System.out.println("Sort by last name - enter 2");
        System.out.println("Sort by address - enter 3");
        System.out.println("Sort by by last edit/create time  - enter 4");
        System.out.println();
        System.out.println("Enter your choose:");
        String sortChoose = sortScanner.nextLine();
        switch (sortChoose) {
            case "1" -> {
                phoneBookDataBase.sort(Comparator.comparing(Contact::getFirstName));
                System.out.println("All contacts by first name sorted successfully");
                System.out.println();
            }
            case "2" -> {
                phoneBookDataBase.sort(Comparator.comparing(Contact::getLastName));
                System.out.println("All contacts by last name sorted successfully");
                System.out.println();
            }
            case "3" -> {
                phoneBookDataBase.sort(Comparator.comparing(Contact::getAddress));
                System.out.println("All contacts by address sorted successfully");
                System.out.println();
            }
            case "4" -> {
                phoneBookDataBase.sort(Comparator.comparing(Contact::getDate));
                System.out.println("All contacts by by last edited/created time sorted successfully");
                System.out.println();
            }
        }
    }

    private static void editContact(Scanner editScanner) {
        System.out.println("Enter first name and last name contact to edit");
        System.out.println("first name");
        String firstNameContactToRemove = editScanner.nextLine();
        System.out.println("last name");
        String lastNameContactToRemove = editScanner.nextLine();
        boolean isContained = false;
        for (int i = 0; i < phoneBookDataBase.size(); i++) {
            if (phoneBookDataBase.get(i).getFirstName().equalsIgnoreCase(firstNameContactToRemove)
                    && phoneBookDataBase.get(i).getLastName().equalsIgnoreCase(lastNameContactToRemove)) {
                System.out.println("Chose what do you want to edit:");
                System.out.println();
                System.out.println("First name and last name - enter 1");
                System.out.println("Number(s) - enter 2");
                System.out.println("Address - enter 3");
                System.out.println("Edit all contact information - enter 4");
                String editChoose = editScanner.nextLine();
                switch (editChoose) {
                    case "1" -> editFirstAndLastName(editScanner, i);
                    case "2" -> editContactPhoneNumber(editScanner, i);
                    case "3" -> editAddress(editScanner, i);
                    case "4" -> {
                        editFirstAndLastName(editScanner, i);
                        editContactPhoneNumber(editScanner, i);
                        editAddress(editScanner, i);
                        System.out.println();
                        System.out.println("All contact information successfully updated");
                    }
                    default -> System.out.println("Incorrect choose");

                }
                isContained = true;
                break;
            }
        }
        if (!isContained) {
            System.out.println("Contact for edit not found");
        }
    }

    private static void editAddress(Scanner editScanner, int i) {
        System.out.println("Enter new address");
        phoneBookDataBase.get(i).setAddress(editScanner.nextLine());
        setDateAndTimeEdition(phoneBookDataBase.get(i));
        System.out.println();
        System.out.println("Address for contact" + phoneBookDataBase.get(i).getFirstName() + " " + phoneBookDataBase.get(i).getLastName() + " successfully update");
        System.out.println();
    }

    private static void editContactPhoneNumber(Scanner editScanner, int i) {
        ArrayList<String> phoneNumbersAfterEdition = new ArrayList<>();
        System.out.println("Enter new number(s) for " + phoneBookDataBase.get(i).getFirstName() + " " + phoneBookDataBase.get(i).getLastName());
        System.out.println("Enter phone number:");
        String phoneNumber = editScanner.nextLine();
        phoneNumbersAfterEdition.add(phoneNumber);
        while (true) {
            System.out.println("Do you want to add another number?(Y/N)");
            String answerToAdd = editScanner.nextLine();
            if (answerToAdd.equals("Y")) {
                System.out.println("Enter number:");
                String anotherPhoneNumber = editScanner.nextLine();
                phoneNumbersAfterEdition.add(anotherPhoneNumber);
            } else if (answerToAdd.equals("N")) {
                break;

            } else {
                System.out.println("Incorrect answer");
            }
        }
        phoneBookDataBase.get(i).setPhoneNumbers(phoneNumbersAfterEdition);
        System.out.println("Phone number(s) for contact " + phoneBookDataBase.get(i).getFirstName() + " " + phoneBookDataBase.get(i).getLastName() + " successfully updated!");
        setDateAndTimeEdition(phoneBookDataBase.get(i));
    }

    private static void editFirstAndLastName(Scanner editScanner, int i) {
        System.out.println("Enter first name");
        phoneBookDataBase.get(i).setFirstName(editScanner.nextLine());
        System.out.println("Enter last name");
        phoneBookDataBase.get(i).setLastName(editScanner.nextLine());
        setDateAndTimeEdition(phoneBookDataBase.get(i));
        System.out.println("First and last name successfully edited");
    }

    private static void deleteContact(Scanner deleteScanner) {
        System.out.println("Enter first name and last name contact to remove");
        System.out.println("first name");
        String firstNameContactToRemove = deleteScanner.nextLine();
        System.out.println("last name");
        String lastNameContactToRemove = deleteScanner.nextLine();
        boolean isContained = false;
        for (int i = 0; i < phoneBookDataBase.size(); i++) {
            if (phoneBookDataBase.get(i).getFirstName().equalsIgnoreCase(firstNameContactToRemove)
                    && phoneBookDataBase.get(i).getLastName().equalsIgnoreCase(lastNameContactToRemove)) {
                phoneBookDataBase.remove(phoneBookDataBase.get(i));
                isContained = true;
                System.out.println("Contact successfully delete");
                System.out.println();
                break;
            }
        }
        if (!isContained) {
            System.out.println("Contact for delete not found");
        }
    }


    private static void saveInDatabase(ObjectMapper mapper) throws JsonProcessingException {
        String contactJson = mapper.writeValueAsString(phoneBookDataBase);
        Path path = Paths.get("database.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(contactJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadContactFromDatabase(ObjectMapper mapper) {
        Path path1 = Paths.get("database.txt");
        try (BufferedReader reader = Files.newBufferedReader(path1)) {
            String line;
            String contactJson1 = "";
            while ((line = reader.readLine()) != null) {
                contactJson1 += line;
            }
            phoneBookDataBase = mapper.readValue(contactJson1, new TypeReference<ArrayList<Contact>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addNewContact(Scanner scanner) {
        Contact contact = new Contact();
        ArrayList<String> phoneNumbers = new ArrayList<>();
        String firstName;
        String lastName;
        System.out.println("Enter first name:");
        firstName = scanner.nextLine();
        System.out.println("Enter last name");
        lastName = scanner.nextLine();

        while (true) {
            int count = 0;
            if (!phoneBookDataBase.isEmpty()) {
                for (int i = 0; i < phoneBookDataBase.size(); i++) {
                    if (phoneBookDataBase.get(i).getFirstName().equals(firstName) && phoneBookDataBase.get(i).getLastName().equals(lastName)) {
                        System.out.println("Contact with this first name is exist, please, enter another");
                        System.out.println("Enter new first name");
                        firstName = scanner.nextLine();
                        System.out.println("Enter last name");
                        lastName = scanner.nextLine();

                    } else {
                        count++;
                    }
                }
                if (count == phoneBookDataBase.size()) {
                    break;
                }

            }
        }


        System.out.println("Enter phone number:");
        String phoneNumber = scanner.nextLine();
        phoneNumbers.add(phoneNumber);

        while (true) {
            System.out.println("Do you want to add another number?(Y/N)");
            String answerToAdd = scanner.nextLine();
            if (answerToAdd.equals("Y")) {
                System.out.println("Enter number:");
                String anotherPhoneNumber = scanner.nextLine();
                phoneNumbers.add(anotherPhoneNumber);
            } else if (answerToAdd.equals("N")) {
                break;

            } else {
                System.out.println("Incorrect answer");
            }
        }
        System.out.println("Enter address");
        String address = scanner.nextLine();

        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setPhoneNumbers(phoneNumbers);
        contact.setAddress(address);
        setDateAndTimeEdition(contact);
        phoneBookDataBase.add(contact);
        System.out.println("Contact successfully added");

    }

    private static void setDateAndTimeEdition(Contact nextCont2) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        nextCont2.setDate(dateFormat.format(cal.getTime()));
    }
}
