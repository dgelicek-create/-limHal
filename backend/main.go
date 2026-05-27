package main

import (
	"encoding/json"
	"fmt"
	"log"
	"os"
	"path/filepath"
	"time"
)

// PrayerTimes represents the daily prayer times.
type PrayerTimes struct {
	Date      string `json:"date"`
	Imsak     int64  `json:"imsak"`
	Gunes     int64  `json:"gunes"`
	Ogle      int64  `json:"ogle"`
	Ikindi    int64  `json:"ikindi"`
	Aksam     int64  `json:"aksam"`
	Yatsi     int64  `json:"yatsi"`
	Timestamp int64  `json:"timestamp"`
}

// CityData represents the complete year of prayer times for a city.
type CityData struct {
	Country   string        `json:"country"`
	City      string        `json:"city"`
	District  string        `json:"district"`
	ID        int           `json:"id"`
	TimesList []PrayerTimes `json:"times_list"`
}

func main() {
	fmt.Println("Starting mock prayer times generation...")

	// Target directory for CDN distribution
	outDir := "./output"
	if err := os.MkdirAll(outDir, os.ModePerm); err != nil {
		log.Fatalf("Failed to create output directory: %v", err)
	}

	// Generating mock data for Turkey/Istanbul
	country := "Turkey"
	city := "Istanbul"
	district := "Istanbul"
	id := 12000

	cityData := CityData{
		Country:   country,
		City:      city,
		District:  district,
		ID:        id,
		TimesList: make([]PrayerTimes, 0, 365),
	}

	// Start date: Today
	now := time.Now()
	startDate := time.Date(now.Year(), now.Month(), now.Day(), 0, 0, 0, 0, time.UTC)

	for i := 0; i < 365; i++ {
		currentDate := startDate.AddDate(0, 0, i)
		
		// Mock logic: 
		// Imsak: 05:00
		// Gunes: 06:30
		// Ogle: 13:00
		// Ikindi: 16:30
		// Aksam: 19:30
		// Yatsi: 21:00
		
		baseTime := currentDate
		imsak := baseTime.Add(5 * time.Hour).UnixMilli()
		gunes := baseTime.Add(6*time.Hour + 30*time.Minute).UnixMilli()
		ogle := baseTime.Add(13 * time.Hour).UnixMilli()
		ikindi := baseTime.Add(16*time.Hour + 30*time.Minute).UnixMilli()
		aksam := baseTime.Add(19*time.Hour + 30*time.Minute).UnixMilli()
		yatsi := baseTime.Add(21 * time.Hour).UnixMilli()
		
		dayData := PrayerTimes{
			Date:      currentDate.Format("2006-01-02"),
			Imsak:     imsak,
			Gunes:     gunes,
			Ogle:      ogle,
			Ikindi:    ikindi,
			Aksam:     aksam,
			Yatsi:     yatsi,
			Timestamp: currentDate.UnixMilli(),
		}
		
		cityData.TimesList = append(cityData.TimesList, dayData)
	}

	// Dump to JSON
	fileName := fmt.Sprintf("%s_%s_%d.json", country, city, id)
	filePath := filepath.Join(outDir, fileName)

	file, err := os.Create(filePath)
	if err != nil {
		log.Fatalf("Failed to create JSON file: %v", err)
	}
	defer file.Close()

	encoder := json.NewEncoder(file)
	encoder.SetIndent("", "  ")
	if err := encoder.Encode(cityData); err != nil {
		log.Fatalf("Failed to encode JSON: %v", err)
	}

	fmt.Printf("Successfully generated %s in %s\n", fileName, outDir)
}
