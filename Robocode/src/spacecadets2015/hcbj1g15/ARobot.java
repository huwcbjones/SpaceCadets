package spacecadets2015.hcbj1g15;

import robocode.AdvancedRobot;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

/**
 * {DESCRIPTION}
 *
 * @author Huw Jones
 * @since 26/11/2015
 */
public class ARobot extends AdvancedRobot {

    @Override
    public void run() {
        while (true) {
            if (getRadarTurnRemaining() == 0.0) {
                setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
            }
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        double angle = getHeadingRadians() + e.getBearingRadians();

        double radarTurn = Utils.normalRelativeAngle(angle - getRadarHeadingRadians());
        double extraTurn = Math.min( Math.atan(40.0 / e.getDistance()), Rules.RADAR_TURN_RATE_RADIANS);

        radarTurn += (radarTurn < 0 ? -extraTurn : extraTurn);

        setTurnRightRadians(radarTurn);
    }
}
