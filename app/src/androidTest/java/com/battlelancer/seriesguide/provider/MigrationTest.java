package com.battlelancer.seriesguide.provider;

import static com.battlelancer.seriesguide.provider.SgRoomDatabase.MIGRATION_42_43;
import static com.battlelancer.seriesguide.provider.SgRoomDatabase.MIGRATION_43_44;
import static com.battlelancer.seriesguide.provider.SgRoomDatabase.MIGRATION_44_45;
import static com.battlelancer.seriesguide.provider.SgRoomDatabase.MIGRATION_45_46;
import static com.battlelancer.seriesguide.provider.SgRoomDatabase.MIGRATION_46_47;
import static com.google.common.truth.Truth.assertThat;

import android.database.sqlite.SQLiteDatabase;
import androidx.room.Room;
import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.battlelancer.seriesguide.dataliberation.model.Show;
import com.battlelancer.seriesguide.model.SgEpisode;
import com.battlelancer.seriesguide.model.SgSeason;
import com.battlelancer.seriesguide.model.SgShow;
import com.battlelancer.seriesguide.thetvdbapi.TvdbImageTools;
import com.uwetrottmann.thetvdb.entities.Episode;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MigrationTest {

    private static final String TEST_DB_NAME = "test-db";

    private static final Show SHOW = new Show();
    private static final SgSeason SEASON = new SgSeason();
    private static final Episode EPISODE = new Episode();

    static {
        SHOW.tvdb_id = 21;
        SHOW.title = "The No Answers Show";
        SHOW.runtime = 45;
        SHOW.poster = "example.jpg";

        SEASON.tvdbId = 21;
        SEASON.showTvdbId = "21";
        SEASON.number = 2;

        EPISODE.id = 21;
        EPISODE.episodeName = "Episode Title";
        EPISODE.airedEpisodeNumber = 1;
    }

    // Helper for creating Room databases and migrations
    @Rule
    public MigrationTestHelper migrationTestHelper =
            new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
                    SgRoomDatabase.class.getCanonicalName(),
                    new FrameworkSQLiteOpenHelperFactory());

    // Helper for creating SQLite database in version 42
    private SqliteTestDbOpenHelper sqliteTestDbHelper;

    @Before
    public void setUp() {
        // delete database file to ensure onCreate is called in SqliteTestDbOpenHelper
        ApplicationProvider.getApplicationContext().deleteDatabase(TEST_DB_NAME);

        // create the database with version 42 using SQLite API
        sqliteTestDbHelper = new SqliteTestDbOpenHelper(ApplicationProvider.getApplicationContext(),
                TEST_DB_NAME);
    }

    @After
    public void tearDown() {
        // close the database to minimize issues when deleting it in setUp()
        sqliteTestDbHelper.close();
    }
    @Ignore
    @Test
    public void migrationFrom42To43_containsCorrectData() throws IOException {
        // Create the database with the initial version 42 schema and insert test data
        insertTestDataSqlite();

        // Re-open the database with version 43 and
        // provide MIGRATION_42_43 as the migration process.
        migrationTestHelper.runMigrationsAndValidate(TEST_DB_NAME, 43,
                false /* adding FTS table ourselves */, MIGRATION_42_43);
        assertTestData(getMigratedRoomDatabase());
    }

    @Ignore
    @Test
    public void migrationFrom42To44_containsCorrectData() throws IOException {
        insertTestDataSqlite();

        migrationTestHelper.runMigrationsAndValidate(TEST_DB_NAME, 44,
                false, MIGRATION_42_43, MIGRATION_43_44);

        // MigrationTestHelper automatically verifies the schema changes, but not the data validity
        // Validate that the data was migrated properly.
        assertTestData(getMigratedRoomDatabase());
    }

    private void insertTestDataSqlite() {
        // Create the database with the initial version 42 schema and insert test data
        SQLiteDatabase db = sqliteTestDbHelper.getWritableDatabase();
        SqliteDatabaseTestHelper.insertShow(SHOW, db);
        SqliteDatabaseTestHelper.insertSeason(SEASON, db);
        SqliteDatabaseTestHelper
                .insertEpisode(EPISODE, SHOW.tvdb_id, SEASON.tvdbId, SEASON.number, db);
        db.close();
    }

    @Ignore
    @Test
    public void migrationFrom43To44_containsCorrectData() throws IOException {
        // First version that uses Room, so can use migration test helper
        SupportSQLiteDatabase db = migrationTestHelper.createDatabase(TEST_DB_NAME, 43);
        RoomDatabaseTestHelper.insertShow(SHOW, db, 43);
        RoomDatabaseTestHelper.insertSeason(SEASON, db);
        RoomDatabaseTestHelper
                .insertEpisode(EPISODE, SHOW.tvdb_id, SEASON.tvdbId, SEASON.number, db);
        db.close();

        assertTestData(getMigratedRoomDatabase());
    }

    @Ignore
    @Test
    public void migrationFrom44To45_containsCorrectData() throws IOException {
        SupportSQLiteDatabase db = migrationTestHelper.createDatabase(TEST_DB_NAME, 44);
        RoomDatabaseTestHelper.insertShow(SHOW, db, 44);
        RoomDatabaseTestHelper.insertSeason(SEASON, db);
        RoomDatabaseTestHelper
                .insertEpisode(EPISODE, SHOW.tvdb_id, SEASON.tvdbId, SEASON.number, db);
        db.close();

        assertTestData(getMigratedRoomDatabase());
    }

    @Ignore
    @Test
    public void migrationFrom45To46_containsCorrectData() throws IOException {
        SupportSQLiteDatabase db = migrationTestHelper.createDatabase(TEST_DB_NAME, 45);
        RoomDatabaseTestHelper.insertShow(SHOW, db, 45);
        RoomDatabaseTestHelper.insertSeason(SEASON, db);
        RoomDatabaseTestHelper
                .insertEpisode(EPISODE, SHOW.tvdb_id, SEASON.tvdbId, SEASON.number, db);
        db.close();

        SgRoomDatabase database = getMigratedRoomDatabase();
        assertTestData(database);
        SgShow dbShow = database.showHelper().getShow();
        assertThat(dbShow.slug).isNull();
    }

    @Ignore
    @Test
    public void migrationFrom46To47_containsCorrectData() throws IOException {
        SupportSQLiteDatabase db = migrationTestHelper.createDatabase(TEST_DB_NAME, 46);
        RoomDatabaseTestHelper.insertShow(SHOW, db, 46);
        RoomDatabaseTestHelper.insertSeason(SEASON, db);
        RoomDatabaseTestHelper
                .insertEpisode(EPISODE, SHOW.tvdb_id, SEASON.tvdbId, SEASON.number, db);
        db.close();

        SgRoomDatabase database = getMigratedRoomDatabase();
        assertTestData(database);
        SgShow dbShow = database.showHelper().getShow();
        assertThat(dbShow.posterSmall).isEqualTo(TvdbImageTools.TVDB_LEGACY_CACHE_PREFIX + dbShow.poster);
    }

    private void assertTestData(SgRoomDatabase database) {
        // MigrationTestHelper automatically verifies the schema changes, but not the data validity.
        // Validate that the data was migrated properly.
        SgShow dbShow = database.showHelper().getShow();
        assertThat(dbShow.tvdbId).isEqualTo(SHOW.tvdb_id);
        assertThat(dbShow.title).isEqualTo(SHOW.title);
        assertThat(dbShow.runtime).isEqualTo(String.valueOf(SHOW.runtime));
        assertThat(dbShow.poster).isEqualTo(SHOW.poster);

        SgSeason dbSeason = database.seasonHelper().getSeason();
        assertThat(dbSeason.tvdbId).isEqualTo(SEASON.tvdbId);
        assertThat(dbSeason.showTvdbId).isEqualTo(SEASON.showTvdbId);
        assertThat(dbSeason.number).isEqualTo(SEASON.number);

        SgEpisode dbEpisode = database.episodeHelper().getEpisode();
        assertThat(dbEpisode.tvdbId).isEqualTo(EPISODE.id);
        assertThat(dbEpisode.showTvdbId).isEqualTo(SHOW.tvdb_id);
        assertThat(dbEpisode.seasonTvdbId).isEqualTo(SEASON.tvdbId);
        assertThat(dbEpisode.title).isEqualTo(EPISODE.episodeName);
        assertThat(dbEpisode.number).isEqualTo(EPISODE.airedEpisodeNumber);
        assertThat(dbEpisode.season).isEqualTo(SEASON.number);
    }

    private SgRoomDatabase getMigratedRoomDatabase() {
        SgRoomDatabase database = Room.databaseBuilder(ApplicationProvider.getApplicationContext(),
                SgRoomDatabase.class, TEST_DB_NAME)
                .addMigrations(
                        MIGRATION_42_43,
                        MIGRATION_43_44,
                        MIGRATION_44_45,
                        MIGRATION_45_46,
                        MIGRATION_46_47
                )
                .build();
        // close the database and release any stream resources when the test finishes
        migrationTestHelper.closeWhenFinished(database);
        return database;
    }
}