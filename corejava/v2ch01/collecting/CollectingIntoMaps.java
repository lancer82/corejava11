package collecting;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * @version 1.00 2016-05-10
 * @author Cay Horstmann
 */
public class CollectingIntoMaps
{

   public static class PersonPlus
   {
      private final int id;
      private final String name;

      public PersonPlus(int id, String name)
      {
         this.id = id;
         this.name = name;
      }

      public int getId()
      {
         return id;
      }

      public String getName()
      {
         return name;
      }

      public String toString()
      {
         return getClass().getName() + "[id=" + id + ",name=" + name + "]";
      }
   }

   public static Stream<PersonPlus> people()
   {
      return Stream.of(new PersonPlus(1001, "Peter"), new PersonPlus(1002, "Paul"),
         new PersonPlus(1003, "Mary"));
   }

   public static void main(String[] args) throws IOException
   {
      Map<Integer, String> idToName = people().collect(
         Collectors.toMap(PersonPlus::getId, PersonPlus::getName));
      System.out.println("idToName: " + idToName);

      Map<Integer, PersonPlus> idToPerson = people().collect(
         Collectors.toMap(PersonPlus::getId, Function.identity()));
      System.out.println("idToPerson: " + idToPerson.getClass().getName()
         + idToPerson);

      idToPerson = people().collect(
         Collectors.toMap(PersonPlus::getId, Function.identity(),
            (existingValue, newValue) -> { throw new IllegalStateException(); }, 
            TreeMap::new));
      System.out.println("idToPerson: " + idToPerson.getClass().getName()
         + idToPerson);

      Stream<Locale> locales = Stream.of(Locale.getAvailableLocales());
      Map<String, String> languageNames = locales.collect(
         Collectors.toMap(
            Locale::getDisplayLanguage, 
            l -> l.getDisplayLanguage(l), 
            (existingValue, newValue) -> existingValue));
      System.out.println("languageNames: " + languageNames);

      locales = Stream.of(Locale.getAvailableLocales());
      Map<String, Set<String>> countryLanguageSets = locales.collect(
         Collectors.toMap(
            Locale::getDisplayCountry,
            l -> Set.of(l.getDisplayLanguage()),
            (a, b) -> 
            { // union of a and b
               Set<String> union = new HashSet<>(a);
               union.addAll(b);
               return union;
            }));
      System.out.println("countryLanguageSets: " + countryLanguageSets);
   }
}
