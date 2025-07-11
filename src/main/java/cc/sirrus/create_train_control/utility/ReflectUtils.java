package cc.sirrus.create_train_control.utility;

import static cc.sirrus.create_train_control.Create_train_control.LOGGER;

public class ReflectUtils {
    public static void safeRun(String description, ReflectiveRunnable action) {
        try {
            action.run();
        } catch (ReflectiveOperationException e) {
            LOGGER.error("反射执行 {} 失败", description, e);
        }
    }
}