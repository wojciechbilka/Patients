package patients;

import javafx.beans.property.*;
import lombok.*;

import java.util.Objects;
import java.util.Random;

public class Patient implements Cloneable {

    @NonNull
    private final StringProperty name = new SimpleStringProperty();
    @NonNull
    private final StringProperty surname = new SimpleStringProperty();
    @NonNull
    private final StringProperty personalNumber = new SimpleStringProperty();
    @NonNull
    private final DoubleProperty balance = new SimpleDoubleProperty();
    @NonNull
    private final ObjectProperty<TestResult> testResult = new SimpleObjectProperty<>();

    public final String getName() {
        return this.name.get();
    }

    public final void setName(String name) {
        this.name.set(name);
    }

    public final StringProperty nameProperty() {
        return name;
    }

    public final String getSurname() {
        return this.surname.get();
    }

    public final void setSurname(String surname) {
        this.surname.set(surname);
    }

    public final StringProperty surnameProperty() {
        return surname;
    }

    public final String getPersonalNumber() {
        return this.personalNumber.get();
    }

    public final void setPersonalNumber(String personalNumber) {
        this.personalNumber.set(personalNumber);
    }

    public final StringProperty personalNumberProperty() {
        return personalNumber;
    }

    public final Double getBalance() {
        return this.balance.get();
    }

    public final void setBalance(Double balance) {
        this.balance.set(balance);
    }

    public final DoubleProperty balanceProperty() {
        return balance;
    }

    public final TestResult getTestResult() {
        return this.testResult.get();
    }

    public final void setTestResult(TestResult testResult) {
        this.testResult.set(testResult);
    }

    public final ObjectProperty<TestResult> testResultProperty() {
        return testResult;
    }

    public Patient(String name, String surname, String personalNumber, Double balance, TestResult testResult) {
        setName(name);
        setSurname(surname);
        setPersonalNumber(personalNumber);
        setBalance(balance);
        setTestResult(testResult);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return getName().equals(patient.getName()) &&
                getSurname().equals(patient.getSurname()) &&
                getPersonalNumber().equals(patient.getPersonalNumber()) &&
                getBalance().equals(patient.getBalance()) &&
                getTestResult().equals(patient.getTestResult());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSurname(), getPersonalNumber(), getBalance(), getTestResult());
    }

    public Patient clone() {
        return new Patient(getName(), getSurname(), getPersonalNumber(), Double.valueOf(getBalance()), getTestResult());
    }

    public boolean isSolvent(double price) {
        return getBalance() >= price;
    }

    public void performTest(double price) {
        if (isSolvent(price)) {
            double balanceTemp = getBalance();
            setBalance(balanceTemp - price);
            setTestResult(getRandomTestResult());
        } else {
            setTestResult(TestResult.NOT_PERFORMED);
        }
    }

    private TestResult getRandomTestResult() {
        Random random = new Random();
        double number = random.nextDouble();
        if (number < 0.3) {
            return TestResult.POSITIVE;
        } else if (number < 0.6) {
            return TestResult.NEGATIVE;
        } else {
            return TestResult.AMBIGUOUS;
        }
    }

    @Override
    public String toString() {
        return "Patient{" +
                "name=" + name +
                ", surname=" + surname +
                ", personalNumber=" + personalNumber +
                ", balance=" + balance +
                ", testResult=" + testResult +
                '}';
    }
}
