package no.nav.tiltak.datadeling;

import org.testcontainers.containers.PostgreSQLContainer;

public class TiltakPostgresContainer extends PostgreSQLContainer<TiltakPostgresContainer> {
    private static final String IMAGE_VERSION = "postgres:17";
    private static TiltakPostgresContainer container;

    private TiltakPostgresContainer() {
        super(IMAGE_VERSION);
    }

    public static TiltakPostgresContainer getInstance() {
        if (container == null) {
            container = new TiltakPostgresContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        //do nothing, JVM handles shut down
    }
}
