package com.jeroenreijn.examples.model;

import java.util.Date;

/**
 * Simple representation of a Presentation
 *
 * @author Jeroen Reijn
 */
public record Presentation(
	long id,
	String title,
	String speakerName,
	String summary
) {}