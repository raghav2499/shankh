package com.wordie.alpha;

import com.wordie.alpha.Color;
import com.wordie.alpha.service.GuessService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class GuessServiceTest {

    @Autowired
    private GuessService guessService;

    @Test
    public void getColorCodingTest() {
        List<Color> colorCodingExpectedResult = Arrays.asList(Color.GREY, Color.GREY, Color.GREY, Color.GREEN, Color.GREEN);
        List<Color> colorCoding = guessService.getColorCoding("green", "haven");
        assert colorCoding.equals(colorCodingExpectedResult);
    }
}
