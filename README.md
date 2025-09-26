# Outdoor Furniture Consultant

A command-line application that helps customers find outdoor furniture through conversational interaction.

## Overview

This project implements a virtual retail assistant for outdoor sitting sets. The program asks customers questions about their needs and space, then recommends furniture from a database of options. The goal was to create something that feels like talking to a real furniture salesperson rather than just filling out a form.

## How It Works

The program follows a simple conversation flow:
1. Asks about space dimensions and seating needs
2. Gets budget and style preferences  
3. Finds out if furniture will stay outside year-round
4. Optionally takes notes about furniture they saw in stores
5. Shows a mock search process
6. Filters products based on their requirements
7. Provides recommendations with explanations

## Key Features

**Natural Conversation**: Instead of just asking for input, the program responds to what users say and gives encouraging feedback.

**Smart Recommendations**: The system considers multiple factors like weather resistance, warranty, and style matching, not just price.

**Detailed Comparisons**: Shows side-by-side tables comparing different options with pros and cons for each.

**Realistic Search Simulation**: Mimics searching actual furniture websites to make the experience feel more authentic.

**Flexible Fallback**: If no perfect matches exist, it relaxes some criteria and explains why.

## Running the Program

```bash
javac Main.java
java Main
```

## Design Decisions

**Needs Before Price**: The program asks about space, seating, and usage patterns before discussing budget. This matches how good salespeople actually work.

**Material Focus**: For year-round outdoor use, the program heavily favors aluminum over steel or wood based on durability research.

**Conversation Style**: I tried to make responses feel human by adding contextual comments and using casual language.

**Mock Search**: Rather than having a static database feel, the simulated search makes it seem like the program is actually checking multiple retailers.

## Technical Implementation

The core logic uses a scoring system:
- Warranty adds points
- Aluminum frame gets bonus points for year-round use
- Style matching gets bonus points
- Price becomes a tiebreaker

The program includes fallback logic that slightly relaxes constraints if no perfect matches are found, then explains this to the user.

## Sample Interaction

```
Outdoor Sitting Set Consultant
Hi there! I'm here to help you find the perfect outdoor furniture. Let's chat!

First, tell me about your outdoor space - how wide is it? (in feet): 8
And how deep/long? (in feet): 8
Nice! A 8.0 x 8.0 ft space gives us some good options to work with.

How many people do you typically want to seat comfortably? 4
Great for family and friends!

[continues with budget, style, and other questions...]

TOP PICK: Harbor Mini Lounge Set
$899 | 4 seats | 6.0×6.0 ft | aluminum frame
Warranty included | coastal style
All-weather durability, compact design
```

## Assumptions Made

- Users are in a climate similar to Toronto for year-round considerations
- The search simulation represents checking major furniture retailers
- The product database represents typical market options
- Command-line interface keeps focus on the conversation logic

## Limitations

The product database is small and hardcoded. In a real system, this would connect to actual retailer APIs. The image analysis is also simplified - users just describe what they saw rather than uploading actual photos and it also checks for a couple key words so it is not accurate.
