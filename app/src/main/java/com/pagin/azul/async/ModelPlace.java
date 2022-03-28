package com.pagin.azul.async;

public class ModelPlace {
	private String placeId;
	private String placeName;
	private String spotName;
	private String region;

	public ModelPlace(String placeId, String placeName, String spotName, String region) {
		super();
		this.placeId = placeId;
		this.placeName = placeName;
		this.spotName=spotName;
		this.region=region;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSpotName() {
		return spotName;
	}

	public void setSpotName(String spotName) {
		this.spotName = spotName;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

}
