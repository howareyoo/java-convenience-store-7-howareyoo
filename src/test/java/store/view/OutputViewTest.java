package store.view;

import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

class OutputViewTest {

    @Test
    void printProducts() throws URISyntaxException {
        OutputView outputView = new OutputView();
        outputView.printProducts();
    }
}