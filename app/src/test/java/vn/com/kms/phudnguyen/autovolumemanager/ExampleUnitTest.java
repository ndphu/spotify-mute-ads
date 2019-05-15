package vn.com.kms.phudnguyen.autovolumemanager;

import org.junit.Assert;
import org.junit.Test;
import vn.com.phudnguyen.tools.autovolumemanager.listener.ListenerService;
import vn.com.phudnguyen.tools.autovolumemanager.listener.database.DatabaseHelper;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.EventAction;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.Rule;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void testMatchingNotification() {
        Rule matchingTitle = Rule.builder()
                .text("Advertisement")
                .build();

        Assert.assertTrue(ListenerService.isRuleMatched(matchingTitle, "Advertisement", "SomeArtist"));
        Assert.assertFalse(ListenerService.isRuleMatched(matchingTitle, "Advertisements", "Advertisement"));

        Rule matchingSubtitle = Rule.builder()
                .subText("Spotify.*")
                .build();

        Assert.assertTrue(ListenerService.isRuleMatched(matchingSubtitle, "", "Spotify"));
        Assert.assertTrue(ListenerService.isRuleMatched(matchingSubtitle, "", "Spotify1"));
        Assert.assertFalse(ListenerService.isRuleMatched(matchingSubtitle, "", "SSpotify"));
    }

    @Test
    public void buildInActionExpressionTest() {
        Assert.assertEquals("()", DatabaseHelper.buildActionsArray(null));
        Assert.assertEquals("()", DatabaseHelper.buildActionsArray(new EventAction[]{}));
        Assert.assertEquals("('MUTED')", DatabaseHelper.buildActionsArray(new EventAction[]{EventAction.MUTED}));
        Assert.assertEquals("('MUTED','SERVICE_STARTED')", DatabaseHelper.buildActionsArray(new EventAction[]{EventAction.MUTED, EventAction.SERVICE_STARTED}));
    }
}