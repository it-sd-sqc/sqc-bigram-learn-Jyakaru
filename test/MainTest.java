import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.chrono.MinguoChronology;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

import edu.cvtc.bigram.*;

@SuppressWarnings({"SpellCheckingInspection"})
class MainTest {
  @Test
  void createConnection() {
    assertDoesNotThrow(
        () -> {
          Connection db = Main.createConnection();
          assertNotNull(db);
          assertFalse(db.isClosed());
          db.close();
          assertTrue(db.isClosed());
        }, "Failed to create and close connection."
    );
  }

  @Test
  void reset() {
    Main.reset();
    assertFalse(Files.exists(Path.of(Main.DATABASE_PATH)));
  }

  @Test
  void mainArgs() {
    assertAll(
        () -> {
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          System.setOut(new PrintStream(out));
          Main.main(new String[]{"--version"});
          String output = out.toString();
          assertTrue(output.startsWith("Version "));
        },
        () -> {
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          System.setOut(new PrintStream(out));
          Main.main(new String[]{"--help"});
          String output = out.toString();
          assertTrue(output.startsWith("Add bigrams"));
        },
        () -> assertDoesNotThrow(() -> {
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          System.setErr(new PrintStream(out));
          Main.main(new String[]{"--reset"});
          String output = out.toString();
          assertTrue(output.startsWith("Expected"));
        }),
        () -> assertDoesNotThrow(() -> Main.main(new String[]{"./sample-texts/non-existant-file.txt"})),
        () -> assertDoesNotThrow(() -> Main.main(new String[]{"./sample-texts/empty.txt"}))
    );
  }

    // TODO: Create your test(s) below. /////////////////////////////////////////
    // should not fail on empty src string on the createbigrams method. What we expect to happen
    // is that it doesn't throw an error
    @Test
    void addBigram() {


        int w0 = 0;
        int w1 = 0;
        assertDoesNotThrow(
                ()-> {
                    // testing for what?
                    // greater than 0, what the id is,
                    Main.reset();
                    Connection db = Main.createConnection(); // create consistant enviroment to test
//                    Main.createBigrams(db,"a");
//                    Main.createBigrams(db, "b");
                    Main.main(new String[]{"./sample-texts/non-existant-file.txt"}); // try adding a txt file
                    Main.addBigram(db,w0,w1);
                    System.out.println(Main.getId(db, "w0"));
                    System.out.println(Main.getId(db,"a"));
                   assertTrue(Main.getId(db, "a") > 0 );
                  // assertTrue(Main.getId(db, "b") == 2);
                   db.close();
                }
        );



    }


    @Test
    void createBigrams() {
      // there are two strings, and so there should be two id's
      String src = "./sample-texts/rinehart-tish.txt"; //
        
        String src1 = "MIND OVER MOTOR\n" +
                "\n" +
                "HOW TISH BROKE THE LAW AND SOME RECORDS";
      assertDoesNotThrow(
              ()-> {
               Main.reset();
               Connection db = Main.createConnection();
               Main.createBigrams(db, src1);
               Scanner scanner = new Scanner(src1) ;
               //assertTrue(Main.getWordCount(db) == 0); // there should only be one word in the db as of this point
                  assertTrue(Main.getId(db,"MOTOR\n") == 3);
                assertTrue(Main.getId(db,"HOW") == 4);
                  assertTrue(Main.getId(db,"\n") == 4);
                  db.close();
                  // \n should be equal to id number 4 because "\n" is a non-whitespace
                  // character which should has an id, which it doensn't because "HOW" is next on the
                  // id list being id number 4, when it should be 5.
                  // this is because the scanner.hasnext() or scanner.next is not scanning \n as a character
                  // on lines 145 and/or 146





              }
      );


    }
}