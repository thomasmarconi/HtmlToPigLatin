import java.util.Scanner;
import java.io.FileReader; //-- for reading single characters at a time
import java.io.FileWriter;
import java.util.Formatter;

public class WebToPigLatin{

  public static void main(String[] args)
  {
    if(args.length != 2)
    {
      System.out.println("Usage: java WebToPigLatin inputFile outputFile");
      System.out.println("Must have two command-line parameters");
      return;
    }
    else
    {
      try{
        FileReader fread = new FileReader(args[0]);
        FileWriter fwrite = new FileWriter(args[1]);
        Scanner scan = new Scanner(fread);
        int nextChar;
        while((nextChar = fread.read()) != -1)
        {
          char c = (char) nextChar;
          if(c == '>')  //we've reached the beginning of text
          {
            boolean inAmpersand = false;
            //System.out.print(c);
            fwrite.write(c);
            StringBuilder str = new StringBuilder();
            while((nextChar = fread.read()) != -1){
              c = (char) nextChar;
              //if(c == ' ' || c == '.' || c == ',' || c == '/' || c == '\n' || c == '(' || c == ')'
              //|| c == '-' || c == ':' || c == '"' || c == ';' || c == '\\' || c == '%')
              if(inAmpersand)
              {
                if(c == ';')
                  inAmpersand = false;
                fwrite.write(c);
                continue;
              }

              if(c == '&')
              {
                inAmpersand = true;
                str = convertWordToPig(str);
                fwrite.write(str.toString());
                fwrite.write(c);
                str = new StringBuilder();
              }
              else if(c == '\'')
                str.append(c);
              else if(c == '<'){
                str = convertWordToPig(str);
                //System.out.print(str);
                fwrite.write(str.toString());
                //System.out.print(c);
                fwrite.write(c);
                break;
              }
              else if(!Character.isLetter(c))
              {
                str = convertWordToPig(str);
                //System.out.print(str);
                fwrite.write(str.toString());
                //System.out.print(c);
                fwrite.write(c);
                str = new StringBuilder();
              }
              else{
                str.append(c);
              }
            }
          }
          else
          {
            //System.out.print(c);
            fwrite.write(c);
          }
        }
        fwrite.close();
        fread.close();

      }catch (Exception ex){
        ex.getStackTrace();
      }
    }
  }

  private static Letter getLetterType(char c, int position){

    if(position == 0) //to treat y as a consonant
    {
      if(Character.isLowerCase(c))
      {
        if(c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u')
          return Letter.LVOWEL;
        else
          return Letter.LCONSONANT;
      }
      else
      {
        if(c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U')
          return Letter.UVOWEL;
        else
          return Letter.UCONSONANT;
      }
    }
    else
    {
      if(Character.isLowerCase(c))
      {
        if(c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == 'y')
          return Letter.LVOWEL;
        else
          return Letter.LCONSONANT;
      }
      else
      {
        if(c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U' || c == 'Y')
          return Letter.UVOWEL;
        else
          return Letter.UCONSONANT;
      }
    }

  }

  private static StringBuilder convertWordToPig(StringBuilder str)
  {
    try{
      if(str == new StringBuilder())
        return new StringBuilder();
      if(str.length() == 1 && getLetterType(str.charAt(0),0) == Letter.UCONSONANT || str.length() == 1 && getLetterType(str.charAt(0),0) == Letter.LCONSONANT)
        return str;
      boolean FirstLetterIsUpper = Character.isUpperCase(str.charAt(0));
      int posOfFirstVowel = -1;
      Letter letterType = Letter.DUMMY;
      for (int i = 0; i<str.length() ;i++ ) {
        letterType = getLetterType(str.charAt(i), i);
        if(letterType == Letter.LVOWEL || letterType == Letter.UVOWEL)
        {
          posOfFirstVowel = i;
          break;
        }
      }
      if(posOfFirstVowel == -1) //found no vowel
        return str;


      if(posOfFirstVowel == 0)
        return str.append("way");
      else{
        if(FirstLetterIsUpper)
        {
          StringBuilder low = new StringBuilder();
          low = low.append(Character.toLowerCase(str.charAt(0)));
          str = low.append(str.substring(1));
        }
        StringBuilder begOfWord = new StringBuilder();
        StringBuilder endOfWord = new StringBuilder();
        for (int i = 0; i < posOfFirstVowel; i++ ) {
          begOfWord.append(str.charAt(i));
        }
        for(int i = posOfFirstVowel; i < str.length(); i++)
        {
          endOfWord.append(str.charAt(i));
        }
        endOfWord.append(begOfWord).append("ay");
        if(FirstLetterIsUpper)
        {
          StringBuilder cap = new StringBuilder();
          cap = cap.append(Character.toUpperCase(endOfWord.charAt(0)));
          cap.append(endOfWord.substring(1));
          return cap;
        }
        else
          return endOfWord;
      }
    }
    catch(Exception ex)
    {
      ex.getStackTrace();
    }return new StringBuilder();
  }

  private enum Letter{
    UVOWEL,
    LVOWEL,
    UCONSONANT,
    LCONSONANT,
    DUMMY
  }
}
