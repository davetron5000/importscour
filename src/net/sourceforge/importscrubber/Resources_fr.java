package net.sourceforge.importscrubber;

public class Resources_fr extends Resources

{

    private static final Object[][] contents = {

                {FILE_BROWSER_TITLE, "Note: les fichiers binaires (.class) et les fichiers sources doivent �tre dans le m�me r�pertoire"},

                {VERSION_ID, "Nettoyeur d'imports 1.4.2"},

                {BROWSE_LABEL, "Naviguer"},

                {GO_LABEL, "Aller"},

                {HELP_LABEL, "Aide"},

                {EXIT_LABEL, "Quitter"},

                {ABOUT_LABEL, "� propos"},

                {RECURSE_LABEL, "R�cursivement"},

                {FILE_LABEL, "Fichiers"},

                {FIND_FILES_LABEL, "Trouver les fichiers"},

                {ALL_DONE, "C'est fini!"},

                {APP_NAME, "Nettoyeur d'imports"},

                {HELP_MESSAGE, "                     Nettoyeur d'imports" + ImportScrubber.LINE_SEPARATOR +  "C'est un utilitaire pour nettoyer les imports. Pour l'utiliser:" + ImportScrubber.LINE_SEPARATOR +  "1) S'assurer que les fichiers sources et les fichiers binaires (.class) sont dans le m�me r�pertoire. " + ImportScrubber.LINE_SEPARATOR +  "2) S�lectionner le fichier contenant du source" + ImportScrubber.LINE_SEPARATOR +  "3) Cliquer sur \"Trouver les fichiers\"" + ImportScrubber.LINE_SEPARATOR +  "4) Cliquer sur \"Ex�cuter\"" + ImportScrubber.LINE_SEPARATOR +  " Nettoyeur d'imports va mouliner pour quelques secondes et montrer un dialogue pour dire que c'est termin�." + ImportScrubber.LINE_SEPARATOR +  " Pour traiter plusieurs fichiers, simplement s�lectionner un r�pertoire et cocher le choix \"R�cursivement\"." + ImportScrubber.LINE_SEPARATOR +  "Questions? Commentaires? Contacter tomcopeland@users.sourceforge.net"},

                {BREAK_EACH_PACKAGE, "Stopper pour chaque package"},

                {BREAK_NONE, "Pas d'arr�ts"},

                {ERR_NOT_DIR, "Le fichier n'est pas un r�pertoire!"},

                {ERR_DIR_NOT_EXIST, "Ce r�pertoire n'existe past!"},

                {ERR_CLASS_FILE_MUST_EXIST, "Le fichier binaire (.class) doit exister:"},

                {ERR_MUST_NOT_BE_DIR, "Le fichier d'entr�e ne peut pas �tre un r�pertoire: "},

                {ERR_UNABLE_TO_FINISH, "Incapable de finir � cause de:"}

            };



    public Object[][] getContents()

    {

        return contents;

    }

}











