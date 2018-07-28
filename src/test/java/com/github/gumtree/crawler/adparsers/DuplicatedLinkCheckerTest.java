package com.github.gumtree.crawler.adparsers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class DuplicatedLinkCheckerTest {

    @Test
    void isAlreadyInDB_shouldFindRawLinkWhenOnlyDecodedLinkIsInDatabase() {
        String decodedLink = "https://www.gumtree.pl/a-mieszkania-i-domy-sprzedam-i-kupie/krakow/mieszkanie-kraków-prądnik-biały-46-36m2-nr-ter ms 9655 6/1002605574960911394550209";
        String rawLink = "https://www.gumtree.pl/a-mieszkania-i-domy-sprzedam-i-kupie/krakow/mieszkanie-krak%C3%B3w-pr%C4%85dnik-bia%C5%82y-46-36m2-nr-ter+ms+9655+6/1002605574960911394550209";
        DuplicatedLinkChecker duplicatedLinkChecker = new DuplicatedLinkChecker(Collections.singletonList(decodedLink));

        boolean alreadyInDB = duplicatedLinkChecker.isAlreadyInDB(rawLink);

        Assertions.assertThat(alreadyInDB).isTrue();

    }
}