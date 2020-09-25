# Raise Your Glass App

![made-with-kotlin](https://img.shields.io/badge/Main%20language-Kotlin-orange) ![what-is-is](https://img.shields.io/badge/What%20is%20it-Android%20App-green) ![NSFW](https://img.shields.io/badge/NSFW-+18%20/%20+21:%20Alcohol-informational) ![finished](https://img.shields.io/badge/finished-First%20version-informational)

## Table of Content
* [What is it?](#what-is-it)
* [Technologies](#technologies)
* [TODO Features](#todo-features)
* [Database Scheme](#database-scheme)

### What is it?
Raise Your Glass is an android app that helps organizing parties.

---

### Technologies
* Kotlin (logic of application)
* XML (layouts of application)
* Firebase

---

### TODO Features
* Register and Login
* List of drinks with ingredients and instruction
* Creating events (public and private), where you can invite your friends in simple way
* Generating shopping list for events

---

### Database Scheme
---
#### Collections:
  * Drinks
  * Favorites
  * Events
---
 
#### Fields in collections:

  * Drinks:
    * Name of Drink (String)
    * Type (String) - is it Shot, Long Drink, etc.
    * Owner - userID(String)
    * Array of Ingredients (Ingredient type)
    * Array of Steps (Step type / String)
    ---
  * Favorites:
    * userID (String)
    * Array of Favorite Drinks (drinkID)
    ---
  * Events:
    * date (Date / Time)
    * place (String)
    * isPrivate (Boolean) - tells if event is private (only owner of this event can invite people to this event) or public (everyone can see this event)
    * owner (userID)
    * Array of objects: - tells which user ordered what type of alcohol
      * userId
      * ordered drinks and other alcohol related stuff (Array of drinkID)
      * comments - other stuff related to the event (alergies, etc)
    * "I'll participate" (Array of userID)
    * "Invited" (Array of userID - only if the event is private)
    
### Contrubitors
* [Davenury](https://github.com/Davenury) (e-mail: dawid199960@gmail.com)
* [Rados13](https://github.com/Rados13) (e-mail: radoslawszuma@gmail.com)
