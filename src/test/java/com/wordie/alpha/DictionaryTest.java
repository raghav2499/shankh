package com.wordie.alpha;

import com.wordie.alpha.utils.Dictionary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class DictionaryTest {

    @Autowired
    private Dictionary dictionary;

    @Test
    public void containsTest() throws IOException {
        assert dictionary.contains("drink");
    }
}
