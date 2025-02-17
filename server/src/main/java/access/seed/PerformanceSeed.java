package access.seed;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PerformanceSeed {

    private static final Log LOG = LogFactory.getLog(PerformanceSeed.class);

    private static final Random random = new Random();

}