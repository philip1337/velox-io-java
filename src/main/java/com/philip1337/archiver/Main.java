package com.philip1337.veloxio.archiver;

import org.apache.commons.cli.*;

class Main {
    /**
     * Entry point (main)
     * @param args String list containing commandline arguments
     */
    public static void main(String args[]) {
        new Main().run(args);
    }

    /**
     * Run
     * @param args String list containing commandline arguments
     */
    public void run(String args[]) {
        // create Options object
        Options options = new Options();
        CommandLine cmd;

        // add t option
        options.addOption("help", false, "Print this message");
        options.addOption("pack", true, "Archive path");
        options.addOption("validate", true, "Validate an archive (name of the file)");
        options.addOption("create", true, "Path to an xml file.");

        // Commandline parser
        CommandLineParser parser = new DefaultParser();
        try {
            cmd = parser.parse( options, args);
        } catch (ParseException e) {
            help(options);
            return;
        }

        // Help
        if (cmd.hasOption("help")) {
            help(options);
            return;
        }

        // Pack action
        if (cmd.hasOption("create")) {
            // No xml defined
            if (!cmd.hasOption("pack")) {
                System.out.println("You need to specify a pack for that action.");
                return;
            }

            new ActionPack(cmd.getOptionValue("create"), 
                           cmd.getOptionValue("pack")).run();
            return;
        }

        // Validate action
        if (cmd.hasOption("validate")) {
            if (!cmd.hasOption("pack")) {
                System.out.println("You need to specify an archive for that action.");
                return;
            }

            new ActionValidate(cmd.getOptionValue("validate"), 
                               cmd.getOptionValue("pack")).run();
            return;
        }
    }

    /**
     * Options
     * @param options
     */
    private void help(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "ant", options );
    }
}