import java.util.*;

public class Main {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		System.out.println("Outdoor Sitting Set Consultant");
		System.out.println("Hi there! I'm here to help you find the perfect outdoor furniture. Let's chat!");
		System.out.println("-------------------------------------------------------------------");

		System.out.print("First, tell me about your outdoor space - how wide is it? (in feet): ");
		double w = readDouble(in, 8);
		System.out.print("And how deep/long? (in feet): ");
		double l = readDouble(in, 8);
		
		System.out.printf("Nice! A %.1f x %.1f ft space gives us some good options to work with.\n\n", w, l);

		System.out.print("How many people do you typically want to seat comfortably? ");
		int seats = readInt(in, 4);
		
		String seatComment = seats <= 3 ? "Perfect for smaller gatherings!" : 
		                    seats <= 5 ? "Great for family and friends!" : 
		                    "Wow, you like to entertain! I'll look for larger sets.";
		System.out.println(seatComment + "\n");

		System.out.print("What's your budget range? (Enter a number, or 0 if you're flexible): $");
		int budget = readInt(in, 0);
		if (budget <= 0) {
			budget = 1_000_000;
			System.out.println("Got it - let's focus on finding the best value regardless of price.\n");
		} else {
			System.out.printf("Perfect! I'll keep it under $%d.\n\n", budget);
		}

		System.out.print("Will this furniture stay outside all year, even in winter? (yes/no): ");
		boolean yearRound = readYesNo(in);
		if (yearRound) {
			System.out.println("Smart to think about that! Weather resistance will be key.\n");
		} else {
			System.out.println("Nice - that gives us more material options since you can store it.\n");
		}

		System.out.print("Any style preferences? I can look for modern, rustic, coastal vibes, or just hit enter to show you everything: ");
		String style = readLine(in, "everything").toLowerCase();
		if (!style.equals("everything") && !style.equals("none")) {
			System.out.printf("Great choice! %s style has some really nice options.\n\n", 
			                 style.substring(0,1).toUpperCase() + style.substring(1));
		} else {
			System.out.println("Perfect - I'll show you a variety of styles!\n");
			style = "none";
		}

		System.out.print("Did you happen to see a set in-store that caught your eye? Describe it briefly (or just press Enter): ");
		String pictureNote = readLine(in, "");
		if (!pictureNote.isEmpty()) {
			System.out.println("Interesting! I'll keep that in mind and see how it compares to what's available online.\n");
		}

		System.out.println("Let me search for options that match what you're looking for...");
		System.out.println("Searching online retailers...");
		simulateSearch();

		// database
		Item[] items = new Item[] {
			new Item("StoreX Coastal Collection", 4, "steel", "polyester", 799, "coastal", 6.5, 6.5, false,
					"Beautiful coastal aesthetic, great value for the look", "Steel frame may show rust over time without care"),
			new Item("Harbor Mini Lounge Set", 4, "aluminum", "solution-dyed", 899, "coastal", 6.0, 6.0, true,
					"All-weather durability, compact design", "Seating feels snug with 4 adults"),
			new Item("Metro L-Sectional Pro", 6, "aluminum", "solution-dyed", 1499, "modern", 9.0, 7.0, true,
					"Sleek modern design, built to last, spacious", "Higher price point, needs larger space"),
			new Item("City Balcony Compact", 3, "aluminum", "polyester", 749, "modern", 5.5, 5.5, false, 
					"Perfect for small spaces, lightweight", "Fabric may fade quicker than premium options"),
			new Item("Cedar Classic Retreat", 4, "wood", "polyester", 799, "rustic", 7.0, 6.0, true, 
					"Warm natural wood feel, comes with warranty", "Requires seasonal maintenance and care"),
			new Item("Weathermaster Deluxe", 5, "aluminum", "solution-dyed", 1299, "modern", 8.0, 6.5, true,
					"Premium all-weather construction, middle ground size", "Investment piece - higher cost"),
			new Item("Coastal Breeze Wicker", 4, "aluminum", "all-weather wicker", 1099, "coastal", 7.5, 6.0, true,
					"Classic wicker look with modern durability", "Wicker requires occasional cleaning"),
			new Item("Rustic Lodge Set", 6, "wood", "olefin", 949, "rustic", 8.5, 7.0, false,
					"Spacious seating, authentic rustic charm", "Wood needs regular staining/sealing")
		};

		// filtering 
		System.out.println("\n Found several options! Filtering based on your needs...\n");
		
		ArrayList<Item> filtered = new ArrayList<>();
		for (Item it : items) {
			boolean fitsSeats = it.seats >= seats;
			boolean fitsBudget = it.price <= budget;
			boolean fitsSpace = it.w <= w + 1 && it.l <= l + 1;
			if (fitsSeats && fitsBudget && fitsSpace)
				filtered.add(it);
		}

		// Fallback with explanation
		if (filtered.isEmpty()) {
			System.out.println("Hmm, let me expand the search a bit...");
			for (Item it : items) {
				boolean fitsSeats = it.seats >= Math.max(1, seats - 1);
				boolean fitsBudget = it.price <= budget + 300;
				boolean fitsSpace = it.w <= w + 2 && it.l <= l + 2;
				if (fitsSeats && fitsBudget && fitsSpace)
					filtered.add(it);
			}
		}

		// Enhanced recommendation logic
		Item best = null;
		int bestScore = -999;
		for (Item it : filtered) {
			int s = 0;
			if (it.warranty) s += 2; // Warranty is valuable
			if (yearRound) {
				if (it.frame.equals("aluminum")) s += 3; // Best for weather
				if (it.frame.equals("steel")) s -= 2; // Rust concerns
				if (it.frame.equals("wood")) s -= 1; // Maintenance needed
			}
			if (!style.equals("none") && it.style.equals(style)) s += 2; // Style match
			if (it.fabric.contains("solution-dyed") || it.fabric.contains("olefin")) s += 1; // Better fabrics
			
			if (best == null || s > bestScore || (s == bestScore && it.price < best.price)) {
				best = it;
				bestScore = s;
			}
		}

		// Enhanced results display
		System.out.println();
		if (filtered.isEmpty()) {
			System.out.println(" I couldn't find perfect matches. Let's try adjusting your budget by $200-300 or consider a slightly smaller set?");
		} else {
			System.out.printf(" Found %d great options for you:\n\n", filtered.size());
			
			// Sort by recommendation score 
			filtered.sort((a, b) -> {
				//  scoring for sort
				int scoreA = (a.warranty ? 2 : 0) + (yearRound && a.frame.equals("aluminum") ? 3 : 0);
				int scoreB = (b.warranty ? 2 : 0) + (yearRound && b.frame.equals("aluminum") ? 3 : 0);
				if (scoreA != scoreB) return scoreB - scoreA;
				return a.price - b.price;
			});
			
			for (int i = 0; i < filtered.size(); i++) {
				Item it = filtered.get(i);
				String ranking = i == 0 ? "⭐ TOP PICK" : i == 1 ? " RUNNER-UP" : " SOLID OPTION";
				
				System.out.printf("%s: %s\n", ranking, it.name);
				System.out.printf("    $%,d |  %d seats |  %.1f×%.1f ft |  %s frame\n", 
				                 it.price, it.seats, it.w, it.l, it.frame);
				System.out.printf("    %s |  %s style\n", 
				                 it.warranty ? "Warranty included" : "No warranty", it.style);
				System.out.printf("    %s\n", it.pros);
				System.out.printf("     %s\n\n", it.cons);
			}

			//  recommendation explanation
			if (best != null) {
				System.out.println(" MY RECOMMENDATION: " + best.name);
				System.out.println("\n Here's why I think this is your best bet:");
				
				ArrayList<String> reasons = new ArrayList<>();
				reasons.add("✓ Fits your space perfectly (" + best.w + "×" + best.l + " ft)");
				reasons.add("✓ Seats " + best.seats + (best.seats > seats ? " (extra room!)" : " comfortably"));
				reasons.add("✓ Within budget at $" + String.format("%,d", best.price));
				
				if (yearRound && best.frame.equals("aluminum")) {
					reasons.add("✓ Aluminum frame is perfect for year-round outdoor use");
				}
				if (best.warranty) {
					reasons.add("✓ Comes with warranty protection");
				}
				if (!style.equals("none") && best.style.equals(style)) {
					reasons.add("✓ Matches your " + style + " style preference");
				}
				
				for (String reason : reasons) {
					System.out.println("   " + reason);
				}
				
				System.out.println("\n IMPORTANT NOTES:");
				if (yearRound) {
					System.out.println("   • Even weather-resistant furniture benefits from covers during harsh storms");
					System.out.println("   • Bring cushions inside during heavy rain/snow");
				}
				if (best.frame.equals("wood")) {
					System.out.println("   • Wood furniture needs seasonal treatment to maintain its look");
				}
				System.out.println("   • Check if assembly is included or if you need to factor that in");
			}
			
			// Detailed comparison table
			if (filtered.size() > 1) {
				System.out.println("\n SIDE-BY-SIDE COMPARISON:");
				System.out.printf("%-25s | %6s | %5s | %8s | %10s | %8s\n", 
				                 "Model", "Price", "Seats", "Material", "Warranty", "Weather");
				System.out.println("".repeat(80).replace("", "-"));
				
				for (Item it : filtered) {
					String weatherRating = yearRound ? 
						(it.frame.equals("aluminum") ? "Excellent" : 
						 it.frame.equals("steel") ? "Good*" : "Fair*") : "N/A";
					
					System.out.printf("%-25s | $%,5d | %5d | %8s | %10s | %8s\n",
					                 it.name.length() > 25 ? it.name.substring(0, 22) + "..." : it.name,
					                 it.price, it.seats, it.frame, 
					                 it.warranty ? "Yes" : "No", weatherRating);
				}
				
				if (yearRound) {
					System.out.println("\n*Good = may need occasional maintenance, Fair = needs regular care");
				}
			}
		}

		// Enhanced picture comment
		if (!pictureNote.isEmpty()) {
			System.out.println("\n ABOUT WHAT YOU SAW IN-STORE:");
			System.out.println("\"" + pictureNote + "\"");
			
			//  give contextual advice based on what they described
			if (pictureNote.toLowerCase().contains("steel") || pictureNote.toLowerCase().contains("metal")) {
				System.out.println("\n If that in-store set was steel and you want year-round use, just keep an eye on rust prevention.");
				System.out.println("Steel can be great, but aluminum alternatives might be more worry-free long-term.");
			}
			if (pictureNote.toLowerCase().contains("wood") || pictureNote.toLowerCase().contains("teak")) {
				System.out.println("\n Wood sets are beautiful! Just factor in the seasonal maintenance if you're keeping it outside year-round.");
			}
			if (pictureNote.toLowerCase().contains("expensive") || pictureNote.toLowerCase().contains("pricey")) {
				System.out.println("\n Sometimes in-store prices are higher than online. The options above might give you similar quality for less!");
			}
		}

		System.out.println("\n Hope this helps! Feel free to run this again with different preferences to explore more options.");
		System.out.println("Happy furniture hunting! ");
		in.close();
	}

	// Mock search simulation 
	static void simulateSearch() {
		String[] sites = {"Wayfair", "Home Depot", "Lowes", "The Brick", "Amazon"};
		try {
			for (String site : sites) {
				System.out.printf("   Checking %s... ", site);
				Thread.sleep(300 + (int)(Math.random() * 200)); // Random delay 300-500ms
				System.out.println("✓");
			}
			Thread.sleep(500);
		} catch (InterruptedException e) {
		}
	}

	// Helper methods 
	static double readDouble(Scanner in, double def) {
		try {
			String input = in.nextLine().trim();
			if (input.isEmpty()) return def;
			return Double.parseDouble(input);
		} catch (Exception e) {
			return def;
		}
	}

	static int readInt(Scanner in, int def) {
		try {
			String input = in.nextLine().trim();
			if (input.isEmpty()) return def;
			return Integer.parseInt(input);
		} catch (Exception e) {
			return def;
		}
	}

	static boolean readYesNo(Scanner in) {
		String s = readLine(in, "no").toLowerCase();
		return s.startsWith("y") || s.equals("yes");
	}

	static String readLine(Scanner in, String def) {
		try {
			String s = in.nextLine();
			if (s == null || s.trim().isEmpty())
				return def;
			return s.trim();
		} catch (Exception e) {
			return def;
		}
	}

	// Item class 
	static class Item {
		String name;
		int seats;
		String frame;
		String fabric;
		int price;
		String style;
		double w;
		double l;
		boolean warranty;
		String pros;
		String cons;

		Item(String name, int seats, String frame, String fabric, int price, String style, double w, double l,
				boolean warranty, String pros, String cons) {
			this.name = name;
			this.seats = seats;
			this.frame = frame;
			this.fabric = fabric;
			this.price = price;
			this.style = style;
			this.w = w;
			this.l = l;
			this.warranty = warranty;
			this.pros = pros;
			this.cons = cons;
		}
	}
}