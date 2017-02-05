import Exceptions.NotFoundException;
import org.apache.commons.dbutils.DbUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * DatabaseAccessObject
 *
 * @author Selcuk Kekec <senycorp@googlemail.com>
 */
public class DAO {
    /**
     * Database Connection Instance
     */
    protected static Connection databaseConnection = null;

    /**
     * User Instance
     */
    protected static Map<String, String> user = null;

    /**
     * Get a database connection
     *
     * @return
     */
    protected static Connection getDatabaseConnection() {
        if (DAO.databaseConnection == null) {
            try {
                // Load Driver
                Class.forName("com.mysql.jdbc.Driver");

                // Create Connection
                DAO.databaseConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/game_of_thrones?user=root&password=MKataturk89");
            } catch (ClassNotFoundException e) {
                System.out.println("Unable to find driver. Please check the dependencies.");
            } catch (SQLException e) {
                System.out.println("Unable to establish a connection to server: ");
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                System.out.println("VendorError: " + e.getErrorCode());
            }
        }

        return DAO.databaseConnection;
    }

    /**
     * Fire query to DBMS
     *
     * @param queryString
     * @return
     * @throws SQLException
     */
    protected static ResultSet query(String queryString) throws SQLException {
        return getDatabaseConnection().createStatement().executeQuery(queryString);
    }

    /**
     * Prepare Statement to prevent SQL injection
     *
     * @param queryString
     * @return
     * @throws SQLException
     */
    protected static PreparedStatement preparedStatement(String queryString) throws SQLException {
        return getDatabaseConnection().prepareStatement(queryString);
    }

    /**
     * Get Häuser
     *
     * @return
     */
    protected static ArrayList<HashMap> getHaeuser() {
        ArrayList<HashMap> results = new ArrayList<>();

        ResultSet resultSet = null;
        try {
            resultSet = query(
                    "SELECT *, haus.id AS id, haus.name AS name, burg.name AS burgName, ort.name AS standortName FROM haus " +
                            "INNER JOIN burg ON haus.sitz_id = burg.id " +
                            "INNER JOIN ort ON burg.standort_id = ort.id " +
                            "WHERE TRUE ORDER BY haus.id DESC LIMIT 5"
            );

            while (resultSet.next()) {
                HashMap<String, String> haus = new HashMap<>();

                haus.put("id", resultSet.getString("id"));
                haus.put("name", resultSet.getString("name"));
                haus.put("motto", resultSet.getString("motto"));
                haus.put("wappen", resultSet.getString("wappen"));
                haus.put("burg", resultSet.getString("burgName"));
                haus.put("standort", resultSet.getString("standortName"));

                results.add(haus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
        }

        return results;
    }

    /**
     * Get Seasons
     *
     * @return
     */
    protected static ArrayList<HashMap> getSeasons() {
        ArrayList<HashMap> results = new ArrayList<>();

        ResultSet resultSet = null;
        try {
            resultSet = query(
                    "SELECT * FROM staffel " +

                            "WHERE TRUE ORDER BY staffel.id DESC LIMIT 5"
            );

            while (resultSet.next()) {
                HashMap<String, String> season = new HashMap<>();

                season.put("id", resultSet.getString("id"));
                season.put("nummer", resultSet.getString("nummer"));
                season.put("startdatum", resultSet.getString("startdatum"));
                season.put("episodenanzahl", resultSet.getString("episodenanzahl"));

                results.add(season);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
        }

        return results;
    }

    /**
     * Get Playlists of user
     *
     * @return
     */
    protected static ArrayList<HashMap> getPlaylists() {
        ArrayList<HashMap> results = new ArrayList<>();

        ResultSet resultSet = null;
        try {
            resultSet = query(
                    "SELECT * FROM (SELECT playlist.id, playlist.titel, count(playlist.id) AS episodenAnzahl FROM playlist \n" +
                            "LEFT JOIN episode_playlist ON episode_playlist.playlist_id = playlist.id \n" +
                            "WHERE besitzer_id = " + getUser().get("id") + " " +
                            "GROUP BY playlist.id) AS grp"
            );

            while (resultSet.next()) {
                HashMap<String, String> playlist = new HashMap<>();

                playlist.put("id", resultSet.getString("id"));
                playlist.put("titel", resultSet.getString("titel"));
                playlist.put("episodenAnzahl", resultSet.getString("episodenAnzahl"));

                results.add(playlist);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
        }

        return results;
    }

    /**
     * Get Animal by ID
     *
     * @param id
     * @return
     */
    protected static HashMap<String, Object> getAnimal(int id) throws NotFoundException {
        HashMap<String, Object> animal = new HashMap<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = preparedStatement(
                    "SELECT *, tier.id AS id, ort.id AS heimatId, ort.name AS heimat, besitzer.name as besitzerName FROM tier " +
                            "INNER JOIN figur  ON   figur.id        =  tier.id " +
                            "INNER JOIN figur besitzer ON   besitzer.id        =  tier.besitzer_id " +
                            "LEFT  JOIN ort    ON   figur.heimat_id =  ort.id " +
                            "WHERE tier.id = ?"
            );

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                animal.put("id", resultSet.getString("id"));
                animal.put("name", resultSet.getString("name"));
                animal.put("heimat", resultSet.getString("heimat"));
                animal.put("heimatId", resultSet.getString("heimatId"));
                animal.put("besitzerName", resultSet.getString("besitzerName"));
                animal.put("besitzerId", resultSet.getString("besitzer_id"));
                animal.put("ratingData", getRatingData(id));
            } else {
                throw new NotFoundException("Unable to find resource");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return animal;
    }

    /**
     * Get Figure by ID
     *
     * @param id
     * @return
     */
    protected static HashMap<String, Object> getPerson(int id) throws NotFoundException {
        HashMap<String, Object> person = new HashMap<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = preparedStatement(
                    "SELECT *, person.id AS id, ort.id AS heimatId, ort.name AS heimat FROM person " +
                            "INNER JOIN figur  ON   figur.id        =  person.id " +
                            "LEFT  JOIN ort    ON   figur.heimat_id =  ort.id " +
                            "WHERE person.id = ?"
            );

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                person.put("id", resultSet.getString("id"));
                person.put("typ", "person");
                person.put("name", resultSet.getString("name"));
                person.put("heimat", resultSet.getString("heimat"));
                person.put("heimatId", resultSet.getString("heimatId"));
                person.put("titel", resultSet.getString("titel"));
                person.put("biografie", resultSet.getString("biografie"));
                person.put("beziehungen", getFigureRelations(id));
                person.put("tiere", getPersonAnimals(id));
                person.put("haeuser", getFigureHaeuser(id));
                person.put("ratingData", getRatingData(id));
            } else {
                throw new NotFoundException("Unable to find resource");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return person;
    }


    /**
     * Get Häuser of Figure
     *
     * @param id
     * @return
     */
    protected static ArrayList<HashMap> getFigureHaeuser(int id) {
        ArrayList<HashMap> haeuser = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = preparedStatement(
                    "SELECT * FROM mitgliedschaft " +
                            "INNER JOIN haus ON haus.id = mitgliedschaft.haus_id " +
                            "WHERE person_id = ?;"
            );

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                HashMap<String, String> haus = new HashMap<>();

                haus.put("id", resultSet.getString("haus_id"));
                haus.put("name", resultSet.getString("name"));
                haus.put("art", resultSet.getString("art"));
                haus.put("von", resultSet.getString("von"));
                haus.put("bis", resultSet.getString("bis"));

                haeuser.add(haus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return haeuser;
    }

    /**
     * Get Relations of Figure
     *
     * @param id
     * @return
     */
    protected static ArrayList<HashMap> getFigureRelations(int id) {
        ArrayList<HashMap> relations = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = preparedStatement(
                    "SELECT *," +
                            "beziehungsart.name AS beziehungsArt, " +
                            "f1.id AS person1Id, " +
                            "f2.id AS person2Id, " +
                            "f1.name AS person1Name, " +
                            "f2.name AS person2Name " +
                            "FROM beziehung " +
                            "INNER JOIN beziehungsart ON beziehung.beziehungsart_id = beziehungsart.id " +
                            "INNER JOIN person p1 ON p1.id = beziehung.person1_id " +
                            "INNER JOIN figur f1 ON p1.id = f1.id " +
                            "INNER JOIN person p2 ON p2.id = beziehung.person2_id " +
                            "INNER JOIN figur f2 ON p2.id = f2.id " +
                            "WHERE beziehung.person1_id = ? OR beziehung.person2_id = ? "
            );

            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                HashMap<String, String> beziehung = new HashMap<>();

                String columnPrefix = "person1";
                if (resultSet.getString("person1Id").equals(String.valueOf(id))) {
                    columnPrefix = "person2";
                }

                beziehung.put("id", resultSet.getString(columnPrefix + "id"));
                beziehung.put("name", resultSet.getString(columnPrefix + "name"));
                beziehung.put("beziehungsArt", resultSet.getString("beziehungsArt"));

                relations.add(beziehung);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return relations;
    }

    /**
     * Get Animals of Person
     *
     * @param id
     * @return
     */
    protected static ArrayList<HashMap> getPersonAnimals(int id) {
        ArrayList<HashMap> animals = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = preparedStatement(
                    "SELECT *" +
                            "FROM tier " +
                            "INNER JOIN figur ON tier.id = figur.id " +
                            "WHERE tier.besitzer_id = ?"
            );

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                HashMap<String, String> animal = new HashMap<>();

                animal.put("id", resultSet.getString("id"));
                animal.put("name", resultSet.getString("name"));

                animals.add(animal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return animals;
    }

    /**
     * Get Haus by ID
     *
     * @param id
     * @return
     */
    protected static HashMap<String, Object> getHaus(int id) throws NotFoundException {
        HashMap<String, Object> haus = new HashMap<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = preparedStatement(
                    "SELECT *, haus.id as id, haus.name AS hausName, burg.id AS burgId, burg.name AS burgName, ort.id AS ortId, ort.name AS ortName FROM haus " +
                            "INNER JOIN burg ON burg.id = haus.sitz_id " +
                            "INNER JOIN ort ON ort.id = burg.standort_id " +
                            "WHERE haus.id = ?"
            );

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                haus.put("id", resultSet.getString("id"));
                haus.put("name", resultSet.getString("hausName"));
                haus.put("burgId", resultSet.getString("burgId"));
                haus.put("burgName", resultSet.getString("burgName"));
                haus.put("ortId", resultSet.getString("ortId"));
                haus.put("ortName", resultSet.getString("ortName"));
                haus.put("members", getHausMembers(id));
                haus.put("properties", getHausProperty(id));
                haus.put("ratingData", getRatingData(id));
            } else {
                throw new NotFoundException("Unable to find resource");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return haus;
    }

    /**
     * Get members of haus
     *
     * @param id
     * @return
     */
    protected static ArrayList<HashMap> getHausMembers(int id) {
        ArrayList<HashMap> members = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = preparedStatement(
                    "SELECT *, person.id AS personId, figur.name AS name " +
                            "FROM mitgliedschaft " +
                            "INNER JOIN person ON person.id = mitgliedschaft.person_id " +
                            "INNER JOIN figur ON figur.id = person.id " +
                            "WHERE mitgliedschaft.haus_id = ?"
            );

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                HashMap<String, String> member = new HashMap<>();

                member.put("id", resultSet.getString("personId"));
                member.put("name", resultSet.getString("name"));

                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return members;
    }

    /**
     * Get property of haus
     *
     * @param id
     * @return
     */
    protected static ArrayList<HashMap> getHausProperty(int id) {
        ArrayList<HashMap> properties = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = preparedStatement(
                    "SELECT * " +
                            "FROM herrschaft " +
                            "INNER JOIN ort ON ort.id = herrschaft.ort_id " +
                            "WHERE herrschaft.haus_id = ?"
            );

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                HashMap<String, String> property = new HashMap<>();

                property.put("id", resultSet.getString("id"));
                property.put("name", resultSet.getString("name"));

                properties.add(property);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return properties;
    }

    /**
     * Get Location by ID
     *
     * @param id
     * @return
     */
    protected static HashMap<String, Object> getLocation(int id) throws NotFoundException {
        HashMap<String, Object> location = new HashMap<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = preparedStatement(
                    "SELECT *, burg.name AS burgName " +
                            "FROM ort " +
                            "LEFT JOIN burg ON burg.standort_id = ort.id " +
                            "WHERE ort.id = ?;"
            );

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                location.put("id", resultSet.getString("id"));
                location.put("name", resultSet.getString("name"));
                location.put("persons", getLocationPersons(id));
                location.put("episodes", getLocationEpisodes(id));
                location.put("haus", getLocationHaus(id));
                location.put("burgName", resultSet.getString("burgName"));
            } else {
                throw new NotFoundException("Unable to find resource");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return location;
    }

    /**
     * Get Persons of location
     *
     * @param id
     * @return
     */
    protected static ArrayList<HashMap> getLocationPersons(int id) {
        ArrayList<HashMap> persons = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = preparedStatement(
                    "SELECT *, person.id AS personId " +
                            "FROM figur " +
                            "LEFT JOIN person ON person.id = figur.id " +
                            "LEFT JOIN tier ON tier.id = figur.id " +
                            "WHERE figur.heimat_id = ?"
            );

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                HashMap<String, String> person = new HashMap<>();

                person.put("id", resultSet.getString("id"));
                person.put("name", resultSet.getString("name"));

                if (resultSet.getString("personId") == null) {
                    person.put("typ", "animal");
                } else {
                    person.put("typ", "person");
                }

                persons.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return persons;
    }

    /**
     * Get Episodes of location
     *
     * @param id
     * @return
     */
    protected static ArrayList<HashMap> getLocationEpisodes(int id) {
        ArrayList<HashMap> episodes = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = preparedStatement(
                    "SELECT *, episode.id AS episodeId, staffel.nummer AS staffelName, episode.titel AS episodeName " +
                            "FROM episode_ort " +
                            "INNER JOIN episode ON episode_ort.episode_id = episode.id " +
                            "INNER JOIN staffel ON episode.staffel_id = staffel.id " +
                            "WHERE episode_ort.ort_id = ?;"
            );

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                HashMap<String, String> episode = new HashMap<>();

                episode.put("id", resultSet.getString("episodeId"));
                episode.put("staffelName", resultSet.getString("staffelName"));
                episode.put("episodeName", resultSet.getString("episodeName"));

                episodes.add(episode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return episodes;
    }

    /**
     * Get Haus of Location
     *
     * @param id
     * @return
     */
    protected static HashMap<String, String> getLocationHaus(int id) throws NotFoundException {
        HashMap<String, String> haus = new HashMap<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = preparedStatement(
                    "SELECT haus.* " +
                            "FROM herrschaft " +
                            "INNER JOIN haus ON haus.id = herrschaft.haus_id " +
                            "WHERE herrschaft.ort_id = ? ORDER BY herrschaft.id DESC LIMIT 1;"
            );

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                haus.put("id", resultSet.getString("id"));
                haus.put("name", resultSet.getString("name"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return haus;
    }

    /**
     * Get Episode by ID
     *
     * @param id
     * @return
     */
    protected static HashMap<String, Object> getEpisode(int id) throws NotFoundException {
        HashMap<String, Object> episode = new HashMap<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = preparedStatement(
                    "SELECT episode.*, staffel.nummer AS staffelName " +
                            "FROM episode " +
                            "INNER JOIN staffel ON staffel.id = episode.staffel_id " +
                            "WHERE episode.id = ?;"
            );

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                episode.put("id", resultSet.getString("id"));
                episode.put("titel", resultSet.getString("titel"));
                episode.put("nummer", resultSet.getString("nummer"));
                episode.put("inhaltsangabe", resultSet.getString("inhaltsangabe"));
                episode.put("figures", getEpisodeFigures(id));
                episode.put("locations", getEpisodeLocations(id));
                episode.put("staffelName", resultSet.getString("staffelName"));
                episode.put("staffelId", resultSet.getString("staffel_id"));
                episode.put("ratingData", getRatingData(id));

            } else {
                throw new NotFoundException("Unable to find resource");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return episode;
    }

    /**
     * Get Figures of Episode
     *
     * @param id
     * @return
     */
    protected static ArrayList<HashMap> getEpisodeFigures(int id) {
        ArrayList<HashMap> figures = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = preparedStatement(
                    "SELECT figur.*, person.id AS personId " +
                            "FROM episode_figur " +
                            "INNER JOIN figur ON figur.id = episode_figur.figur_id " +
                            "LEFT JOIN person ON figur.id = person.id " +
                            "LEFT JOIN tier ON figur.id = tier.id " +
                            "WHERE episode_figur.episode_id = ?;"
            );

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                HashMap<String, String> figure = new HashMap<>();

                figure.put("id", resultSet.getString("id"));
                figure.put("name", resultSet.getString("name"));

                if (resultSet.getString("personId") == null) {
                    figure.put("typ", "animal");
                } else {
                    figure.put("typ", "person");
                }

                figures.add(figure);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return figures;
    }

    /**
     * Get Locations of Episode
     *
     * @param id
     * @return
     */
    protected static ArrayList<HashMap> getEpisodeLocations(int id) {
        ArrayList<HashMap> locations = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = preparedStatement(
                    "SELECT ort.* " +
                            "FROM episode_ort " +
                            "INNER JOIN ort ON ort.id = episode_ort.ort_id " +
                            "WHERE episode_ort.episode_id = ?;"
            );

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                HashMap<String, String> location = new HashMap<>();

                location.put("id", resultSet.getString("id"));
                location.put("name", resultSet.getString("name"));

                locations.add(location);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return locations;
    }

    /**
     * Get Season by ID
     *
     * @param id
     * @return
     */
    protected static HashMap<String, Object> getSeason(int id) throws NotFoundException {
        HashMap<String, Object> season = new HashMap<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = preparedStatement(
                    "SELECT * " +
                            "FROM staffel " +
                            "WHERE staffel.id = ? ;"
            );

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                season.put("id", resultSet.getString("id"));
                season.put("nummer", resultSet.getString("nummer"));
                season.put("startdatum", resultSet.getString("startdatum"));
                season.put("episodenanzahl", resultSet.getString("episodenanzahl"));
                season.put("episodes", getSeasonEpisodes(id));
                season.put("ratingData", getRatingData(id));
            } else {
                throw new NotFoundException("Unable to find resource");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return season;
    }

    /**
     * Get Episodes of Season
     *
     * @param id
     * @return
     */
    protected static ArrayList<HashMap> getSeasonEpisodes(int id) {
        ArrayList<HashMap> episodes = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = preparedStatement(
                    "SELECT * " +
                            "FROM episode " +
                            "WHERE episode.staffel_id = ?"
            );

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                HashMap<String, String> episode = new HashMap<>();

                episode.put("id", resultSet.getString("id"));
                episode.put("titel", resultSet.getString("titel"));
                episode.put("nummer", resultSet.getString("nummer"));
                episode.put("erstausstrahlungsdatum", resultSet.getString("erstausstrahlungsdatum"));

                episodes.add(episode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return episodes;
    }

    /**
     * Search figures
     *
     * @param id
     * @return
     */
    protected static ArrayList<HashMap> searchFigures(String keyword) {
        ArrayList<HashMap> rows = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String queryString = "SELECT *, person.id AS isPerson, ort.name AS heimat " +
                    "FROM figur " +
                    "LEFT JOIN person ON person.id = figur.id " +
                    "LEFT JOIN tier ON tier.id = figur.id " +
                    "INNER JOIN ort ON ort.id = figur.heimat_id " +
                    "WHERE   figur.name LIKE ? OR " +
                    "person.titel LIKE ? OR " +
                    "person.biografie LIKE ? ";

            if (keyword.equals("")) {
                queryString = queryString.concat(" OR TRUE");
            } else {
                keyword = "%" + keyword + "%";
            }

            preparedStatement = preparedStatement(
                    queryString
            );

            preparedStatement.setString(1, keyword);
            preparedStatement.setString(2, keyword);
            preparedStatement.setString(3, keyword);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                HashMap<String, String> row = new HashMap<>();

                row.put("id", resultSet.getString("id"));
                row.put("name", resultSet.getString("name"));
                row.put("heimat", resultSet.getString("heimat"));

                if (resultSet.getString("isPerson") != null) {
                    row.put("typ", "person");
                    row.put("titel", resultSet.getString("titel"));
                    row.put("biografie", resultSet.getString("biografie"));
                } else {
                    row.put("typ", "animal");
                }

                rows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return rows;
    }

    /**
     * Search Haeuser
     *
     * @param id
     * @return
     */
    protected static ArrayList<HashMap> searchHaeuser(String keyword) {
        ArrayList<HashMap> rows = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String queryString = "SELECT *, haus.id AS id, haus.name AS name, burg.name AS burgName, ort.name AS standortName FROM haus " +
                    "INNER JOIN burg ON haus.sitz_id = burg.id " +
                    "INNER JOIN ort ON burg.standort_id = ort.id " +
                    "WHERE haus.name LIKE ? OR haus.motto LIKE ? ";

            if (keyword.equals("")) {
                queryString = queryString.concat(" OR TRUE");
            } else {
                keyword = "%" + keyword + "%";
            }

            preparedStatement = preparedStatement(queryString);

            preparedStatement.setString(1, keyword);
            preparedStatement.setString(2, keyword);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                HashMap<String, String> row = new HashMap<>();

                row.put("id", resultSet.getString("id"));
                row.put("name", resultSet.getString("name"));
                row.put("motto", resultSet.getString("motto"));
                row.put("wappen", resultSet.getString("wappen"));
                row.put("burg", resultSet.getString("burgName"));
                row.put("standort", resultSet.getString("standortName"));

                rows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return rows;
    }

    /**
     * Search Seasons
     *
     * @param id
     * @return
     */
    protected static ArrayList<HashMap> searchSeasons(String keyword) {
        ArrayList<HashMap> rows = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String queryString = "SELECT * FROM staffel " +
                    "WHERE nummer LIKE ? OR episodenanzahl LIKE ? ";

            if (keyword.equals("")) {
                queryString = queryString.concat(" OR TRUE");
            } else {
                keyword = "%" + keyword + "%";
            }

            preparedStatement = preparedStatement(queryString);

            preparedStatement.setString(1, keyword);
            preparedStatement.setString(2, keyword);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                HashMap<String, String> row = new HashMap<>();

                row.put("id", resultSet.getString("id"));
                row.put("nummer", resultSet.getString("nummer"));
                row.put("startdatum", resultSet.getString("startdatum"));
                row.put("episodenanzahl", resultSet.getString("episodenanzahl"));

                rows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return rows;
    }

    /**
     * Get Episodes of Season
     *
     * @param id
     * @return
     */
    protected static HashMap<String, Object> getRatingData(int id) {
        HashMap<String, Object> data = new HashMap<>();
        ArrayList<HashMap> ratings = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        float totalRating = 0f;
        try {
            preparedStatement = preparedStatement(
                    "SELECT * " +
                            "FROM bewertung " +
                            "INNER JOIN benutzer ON benutzer.id = bewertung.bewerter_id " +
                            "WHERE bewertung.bewertbar_id = ?"
            );

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                HashMap<String, String> rating = new HashMap<>();

                rating.put("id", resultSet.getString("id"));
                rating.put("rating", resultSet.getString("rating"));
                rating.put("name", resultSet.getString("name"));
                rating.put("text", resultSet.getString("text"));

                totalRating += (float)resultSet.getInt("rating");

                ratings.add(rating);
            }

            data.put("id", id);
            data.put("data", ratings);
            data.put("totalRating", totalRating / ratings.size());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return data;
    }

    /**
     * Save Rating
     *
     * @param rating
     * @param text
     * @return
     */
    protected static boolean saveRating(int id, int rating, String text) throws SQLException {

        PreparedStatement preparedStatement = null;
        PreparedStatement p1 = null;
        PreparedStatement p2 = null;
        try {
            preparedStatement = preparedStatement(
                    "SELECT * FROM bewertung " +
                            "WHERE bewertbar_id = ? AND bewerter_id = ?;"
            );

            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, Integer.parseInt(getUser().get("id")));

            ResultSet resultSet = preparedStatement.executeQuery();

            // Disable auto commit
            getDatabaseConnection().setAutoCommit(false);

            // Check whether a rating exists
            if (resultSet.next()) {
                p1 = preparedStatement("DELETE FROM bewertung WHERE bewertbar_id = ? AND bewerter_id = ?;");
                p1.setInt(1, id);
                p1.setInt(2, Integer.parseInt(getUser().get("id")));

                p1.executeUpdate();
            }

            p2 = preparedStatement("INSERT INTO bewertung VALUES(NULL, ?, ?, ?, ?)");
            p2.setInt(1, id);
            p2.setInt(2, Integer.parseInt(getUser().get("id")));
            p2.setInt(3, rating);
            p2.setString(4, text);
            p2.executeUpdate();

            try {
                getDatabaseConnection().commit();
                return true;
            } catch (SQLException eSQL) {
                getDatabaseConnection().rollback();
                eSQL.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();

            throw e;
        } finally {
            DbUtils.closeQuietly(preparedStatement);
            DbUtils.closeQuietly(p1);
            DbUtils.closeQuietly(p2);
        }

        return false;
    }

    /**
     * Get Playlist by ID
     *
     * @param id
     * @return
     */
    protected static HashMap<String, Object> getPlaylist(int id) throws NotFoundException {
        /**
         * @todo
         * Check for AUTH
         */

        HashMap<String, Object> playlist = new HashMap<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = preparedStatement(
                    "SELECT * " +
                            "FROM playlist " +
                            "WHERE playlist.id = ? ;"
            );

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                playlist.put("id", resultSet.getString("id"));
                playlist.put("titel", resultSet.getString("titel"));
                playlist.put("episodes", getPlaylistEpisodes(id));
            } else {
                throw new NotFoundException("Unable to find resource");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return playlist;
    }

    /**
     * Get Episodes of Season
     *
     * @param id
     * @return
     */
    protected static HashMap<String, Object> getPlaylistEpisodes(int id) {
        HashMap<String, Object> data = new HashMap<>();
        ArrayList<HashMap> episodes = new ArrayList<>();
        ArrayList<String> seasons = new ArrayList<>();

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = preparedStatement(
                    "SELECT episode.*, staffel.nummer AS staffelNummer " +
                            "FROM episode_playlist " +
                            "INNER JOIN episode ON episode.id = episode_playlist.episode_id " +
                            "INNER JOIN staffel ON staffel.id = episode.staffel_id " +
                            "WHERE episode_playlist.playlist_id = ? ORDER BY staffel.nummer ASC"
            );

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                HashMap<String, String> episode = new HashMap<>();

                episode.put("id", resultSet.getString("id"));
                episode.put("titel", resultSet.getString("titel"));
                episode.put("nummer", resultSet.getString("nummer"));
                episode.put("staffelNummer", resultSet.getString("staffelNummer"));
                episode.put("erstausstrahlungsdatum", resultSet.getString("erstausstrahlungsdatum"));

                if (!seasons.contains(resultSet.getString("staffelNummer"))) seasons.add(resultSet.getString("staffelNummer"));

                episodes.add(episode);
            }

            data.put("data", episodes);
            data.put("seasons", seasons);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(preparedStatement);
        }

        return data;
    }

    /**
     * Create a new playlist
     *
     * @param title
     * @return
     */
    protected static boolean createPlaylist(String title) {
        PreparedStatement preparedStatement = null;
        boolean ret = false;
        try {
            preparedStatement = preparedStatement(
                    "INSERT INTO playlist VALUES(NULL, ?, ?);"
            );

            preparedStatement.setString(1, title);
            preparedStatement.setString(2, getUser().get("id"));
            preparedStatement.executeUpdate();

            ret = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(preparedStatement);
        }

        return ret;
    }

    /**
     * Get Figures
     *
     * @return
     */
    protected static ArrayList<HashMap> getFigures() {
        ArrayList<HashMap> results = new ArrayList();

        ResultSet resultSet = null;
        try {
            resultSet = query(
                    "SELECT *, person.id AS id, ort.name AS heimat FROM person " +
                            "INNER JOIN figur  ON   figur.id        =  person.id " +
                            "LEFT  JOIN ort    ON   figur.heimat_id =  ort.id " +
                            "WHERE TRUE ORDER BY person.id DESC LIMIT 5"
            );

            while (resultSet.next()) {
                HashMap<String, String> person = new HashMap<>();

                person.put("id", resultSet.getString("id"));
                person.put("typ", "person");
                person.put("name", resultSet.getString("name"));
                person.put("heimat", resultSet.getString("heimat"));
                person.put("titel", resultSet.getString("titel"));
                person.put("biografie", resultSet.getString("biografie"));

                results.add(person);
            }

            resultSet = query(
                    "SELECT tier.id as id, " +
                            "besitzer.name AS besitzerName, " +
                            "person.titel AS besitzerTitel, " +
                            "tierFigur.name AS tierName,  " +
                            "ort.name AS heimat " +
                            "FROM tier " +
                            "INNER JOIN figur   tierFigur           ON   tierFigur.id                   =  tier.id " +
                            "LEFT  JOIN ort                         ON   tierFigur.heimat_id            =  ort.id " +
                            "LEFT  JOIN  person                     ON   person.id                      =  tier.besitzer_id " +
                            "LEFT  JOIN  figur besitzer             ON   person.id                      =  besitzer.id " +
                            "WHERE TRUE ORDER BY person.id DESC LIMIT 5"
            );

            while (resultSet.next()) {
                HashMap<String, String> tier = new HashMap<>();

                tier.put("id", resultSet.getString("id"));
                tier.put("typ", "tier");
                tier.put("name", resultSet.getString("tierName"));
                tier.put("besitzer", resultSet.getString("besitzerName"));
                tier.put("besitzerTitel", resultSet.getString("besitzerTitel"));
                tier.put("heimat", resultSet.getString("heimat"));

                results.add(tier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(resultSet);
        }

        return results;
    }

    /**
     * Get user
     *
     * @return
     */
    protected static Map<String, String> getUser() {
        if (DAO.user == null) {
            ResultSet resultSet = null;
            try {
                resultSet = DAO.query("SELECT * FROM benutzer WHERE id = 1;");
                resultSet.next();
                DAO.user = new HashMap<>();
                DAO.user.put("id", resultSet.getString("id"));
                DAO.user.put("name", resultSet.getString("name"));
                DAO.user.put("username", resultSet.getString("login_kennung"));
                DAO.user.put("password", resultSet.getString("passwort"));

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DbUtils.closeQuietly(resultSet);
            }
        }

        return DAO.user;
    }
}
