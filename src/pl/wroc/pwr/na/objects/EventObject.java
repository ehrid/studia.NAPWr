package pl.wroc.pwr.na.objects;

import java.util.Date;

public class EventObject {

	
	public CharSequence name;
	public int id;
	public CharSequence content;
	public CharSequence externalLink;
	public OrganizationObject organization;
	public AddressObject address;
	public DepartmentObject department;
	public CharSequence poster;
	public CharSequence tag1;
	public CharSequence tag2;
	public CharSequence tag3;
	public CharSequence tag4;
	public CharSequence tag5;
	public int likeSum;
	public boolean isLiked;
	public Date startDate;
	public Date endDate;
	public Date addedDate;
	public Date publicationDate;
	public boolean isPayable;
	public Double price;
	public CharSequence room;
	public Date actualizationDate;
	public boolean enrollment;
	public Date enrollmentStartDate;
	public Date enrollmentEndDate;
	public CharSequence enrollmentLink;
	public CharSequence facebookLink;
	public boolean isAllDay;
	public CharSequence youtubeVideo;
	public int places;
	public OrganizationObject organization2;
	public OrganizationObject organization3;
	public OrganizationObject organization4;
	public OrganizationObject organization5;
	public CharSequence speaker1;
	public CharSequence speaker2;
	public CharSequence speaker3;
	public CharSequence googlePlusLink;
	public CharSequence agendaLink;
	
	
	public EventObject(CharSequence name, int id) {
		this.name = name;
		this.id = id;
	}

	public EventObject(CharSequence name, int id, boolean isLiked) {
		super();
		this.name = name;
		this.id = id;
		this.isLiked = isLiked;
	}
	
	public EventObject(CharSequence name, int id, CharSequence content,
			CharSequence externalLink, OrganizationObject organization,
			AddressObject address, DepartmentObject department,
			CharSequence poster, CharSequence tag1, CharSequence tag2,
			CharSequence tag3, CharSequence tag4, CharSequence tag5,
			int likeSum, Date startDate, Date endDate, Date addedDate,
			Date publicationDate, boolean isPayable, Double price,
			CharSequence room, Date actualizationDate, boolean enrollment,
			Date enrollmentStartDate, Date enrollmentEndDate,
			CharSequence enrollmentLink, CharSequence facebookLink,
			boolean isAllDay, CharSequence youtubeVideo, int places,
			OrganizationObject organization2, OrganizationObject organization3,
			OrganizationObject organization4, OrganizationObject organization5,
			CharSequence speaker1, CharSequence speaker2,
			CharSequence speaker3, CharSequence googlePlusLink,
			CharSequence agendaLink) {
		this.name = name;
		this.id = id;
		this.content = content;
		this.externalLink = externalLink;
		this.organization = organization;
		this.address = address;
		this.department = department;
		this.poster = poster;
		this.tag1 = tag1;
		this.tag2 = tag2;
		this.tag3 = tag3;
		this.tag4 = tag4;
		this.tag5 = tag5;
		this.likeSum = likeSum;
		this.startDate = startDate;
		this.endDate = endDate;
		this.addedDate = addedDate;
		this.publicationDate = publicationDate;
		this.isPayable = isPayable;
		this.price = price;
		this.room = room;
		this.actualizationDate = actualizationDate;
		this.enrollment = enrollment;
		this.enrollmentStartDate = enrollmentStartDate;
		this.enrollmentEndDate = enrollmentEndDate;
		this.enrollmentLink = enrollmentLink;
		this.facebookLink = facebookLink;
		this.isAllDay = isAllDay;
		this.youtubeVideo = youtubeVideo;
		this.places = places;
		this.organization2 = organization2;
		this.organization3 = organization3;
		this.organization4 = organization4;
		this.organization5 = organization5;
		this.speaker1 = speaker1;
		this.speaker2 = speaker2;
		this.speaker3 = speaker3;
		this.googlePlusLink = googlePlusLink;
		this.agendaLink = agendaLink;
	}

	public EventObject(CharSequence name, int id, CharSequence content,
			CharSequence poster, int likeSum, Date startDate) {
		super();
		this.name = name;
		this.id = id;
		this.content = content;
		this.poster = poster;
		this.likeSum = likeSum;
		this.startDate = startDate;
	}

}