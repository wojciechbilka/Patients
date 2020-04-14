package patients;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assumptions.assumingThat;

class PatientTest {

    private Patient patient = new Patient("Ron", "Bron", "12346666", 2000.0, TestResult.NOT_PERFORMED);

    @AfterEach
    private void replacePatientFieldObject() {
        patient = new Patient("Ron", "Bron", "12346666", 2000.0, TestResult.NOT_PERFORMED);
    }

    @Test
    void isSolventMethodShouldReturnTrueIfPatientIsSolvent() {
        // given when then
        assertThat(patient.isSolvent(200), is(true));
    }

    @Test
    void afterPerformingTestPatientsBalanceShouldDecreaseByTesCost() {
        // given
        Double beforeTest = Double.valueOf(patient.getBalance());

        // when
        patient.performTest(200);

        // then
        assertThat(patient.getBalance(), is(equalTo(beforeTest - 200)));
    }

    @Test
    void afterPerformingTestPatientsTestResultShouldChangeIfNotPerformed() {
        // given
        TestResult testResultBefore = patient.getTestResult();

        // when
        patient.performTest(200);

        // then
        assumingThat(testResultBefore.equals(TestResult.NOT_PERFORMED), () -> {
            assertThat(patient.getTestResult(), is(not(equalTo(testResultBefore))));
        });
    }
}