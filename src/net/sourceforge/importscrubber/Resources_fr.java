package net.sourceforge.importscrubber;

public class Resources_fr extends Resources
{
   private static final Object[][] contents = {
      {FILE_BROWSER_TITLE, "Note: les fichiers binaires (.class) et les fichiers sources doivent être dans le même répertoire"},
      {VERSION_ID, "Nettoyeur d'imports 1.3.9"},
      {BROWSE_LABEL, "Naviguer"},
      {GO_LABEL, "Aller"},
      {HELP_LABEL, "Aide"},
      {EXIT_LABEL, "Quitter"},
      {ABOUT_LABEL, "À propos"},
      {RECURSE_LABEL, "Récursivement"},
      {FILE_LABEL, "Fichiers"},
      {FIND_FILES_LABEL, "Trouver les fichiers"},
      {ALL_DONE, "C'est fini!"},
      {APP_NAME, "Nettoyeur d'imports"},
      {HELP_MESSAGE, "                     Nettoyeur d'imports\nC'est un utilitaire pour nettoyer les imports. Pour l'utiliser:\n1) S'assurer que les fichiers sources et les fichiers binaires (.class) sont dans le même répertoire. \n2) Sélectionner le fichier contenant du source\n3) Cliquer sur \"Trouver les fichiers\"\n4) Cliquer sur \"Exécuter\"\n Nettoyeur d'imports va mouliner pour quelques secondes et montrer un dialogue pour dire que c'est terminé.\n Pour traiter plusieurs fichiers, simplement sélectionner un répertoire et cocher le choix \"Récursivement\".\nQuestions? Commentaires? Contacter tomcopeland@users.sourceforge.net"},
      {BREAK_EACH_PACKAGE, "Stopper pour chaque package"},
      {BREAK_TOP_PACKAGE, "Stopper pour les packages principaux"},
      {BREAK_NONE, "Pas d'arrêts"},
      {ERR_NOT_DIR, "Le fichier n'est pas un répertoire!"},
      {ERR_DIR_NOT_EXIST, "Ce répertoire n'existe past!"},
      {ERR_CLASS_FILE_MUST_EXIST, "Le fichier binaire (.class) doit exister:"},
      {ERR_MUST_NOT_BE_DIR, "Le fichier d'entrée ne peut pas être un répertoire: "},
      {ERR_UNABLE_TO_FINISH, "Incapable de finir à cause de:"}
   };

   public Object[][] getContents() 
   {
      return contents;
   }
}


