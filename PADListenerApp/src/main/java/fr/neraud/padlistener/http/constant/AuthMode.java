package fr.neraud.padlistener.http.constant;

/**
 * Authentication modes supported
 *
 * @author Neraud
 */
public enum AuthMode {
	// Basic Authentication
	BASIC,
	// X-Username + X-Password custom Authentication for PADherder
	X_HEADER
}