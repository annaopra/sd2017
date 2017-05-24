import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import fsega.distributedsystems.server.util.OutputBuilder;
import fsega.distributedsystems.server.util.ParsedUrl;
import fsega.distributedsystems.server.util.ServiceAgregator;
import fsega.distributedsystems.server.util.exceptions.NoSuchServiceException;
import fsega.distributedsystems.server.util.exceptions.ParameterNotFoundException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ ParsedUrl.class, ServiceAgregator.class })
public class OutputBuilderTest {

    @Before
    public void setUp() {
        mockStatic(ParsedUrl.class);
        mockStatic(ServiceAgregator.class);
    }

    @Test
    public void itShouldReturnJsonToString() throws ParameterNotFoundException, NoSuchServiceException {
        // Given
        final String url = "S1?begindate=xx&enddate=xx&symbol1=xx&symbol2=xx&exchange=xx";
        ParsedUrl parsedUrlMock = new ParsedUrl("testService", "beginDate", "endDate", "s1", "s2", "exchange");
        when(ParsedUrl.fromRequestUrl(url)).thenReturn(parsedUrlMock);
        final String serviceResult = "testCoefficent|testInterpretation";
        when(ServiceAgregator.getDataFromUrl(parsedUrlMock)).thenReturn(serviceResult);

        JSONObject expectedJsonObject = new JSONObject();
        expectedJsonObject.put("SERVICE_NAME", "testService");
        expectedJsonObject.put("SERVICE_CODE", "testService");
        expectedJsonObject.put("START_DATE", parsedUrlMock.getBeginDate());
        expectedJsonObject.put("EXCHANGE", parsedUrlMock.getExchange());
        expectedJsonObject.put("END_DATE", parsedUrlMock.getEndDate());
        expectedJsonObject.put("SYM1", parsedUrlMock.getSymbol1());
        expectedJsonObject.put("SYM2", parsedUrlMock.getSymbol2());
        expectedJsonObject.put("CORRELATION_COEFFIECIENT", serviceResult.split("\\|")[0]);
        expectedJsonObject.put("INTERPRETATION", serviceResult.split("\\|")[1]);

        // When
        final String result = OutputBuilder.getJsonForUrl(url);

        // Then
        verifyStatic();
        ParsedUrl.fromRequestUrl(url);
        verifyStatic();
        ServiceAgregator.getDataFromUrl(parsedUrlMock);
        assertEquals(expectedJsonObject.toString(), result);
    }

}
