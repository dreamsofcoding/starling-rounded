# starling-rounded


A jetpack Compose MVVM project utilising the Starling Bank developers API services to build a small 
round up feature. - Mark Prime

## Overview

The project has a few screens, Access Token Entry, RoundUpView 

## Access Token Entry

Here any developer can enter their access token and use the functionality of the app.

## RoundUpView

This consists of a few Jetpack Compose elements to handle the UI for the customer. Including a 

## FullScreenLoader
A full screen loader presented to the customer as all the details for the RoundUpView are prepared as the requests and responses of the backend API are processed.

## RoundUpError
An error screen that details as much as possible what error has been encountered in the requested operations.

## RoundUpLoaded
The main RoundUpView, with all the loaded details and UI components for the customer to make decisions about how they perform their round up.

## RoundUpTransferComplete

The confirmation screen allowing the customer to see the details of their round up transfer and providing them options to return back to the RoundUpView and perform another RoundUp.

