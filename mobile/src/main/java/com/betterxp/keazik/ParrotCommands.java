package com.betterxp.keazik;

public class ParrotCommands {

	private static final String GET_VERSION = "/api/software/version/get";
	private static final String GET_FRIENDLY_NAME = "/api/bluetooth/friendlyname/get";
	private static final String GET_AUTO_CONNECTION = "/api/system/auto_connection/enabled/get";
	private static final String GET_ANC_PHONE_MODE = "/api/system/anc_phone_mode/enabled/get";
	private static final String GET_NOISE_CANCEL = "/api/audio/noise_cancellation/enabled/get";
	private static final String GET_LOU_REED_MODE = "/api/audio/specific_mode/enabled/get";
	private static final String GET_CONCERT_HALL = "/api/audio/sound_effect/enabled/get";

	private static final String SET_SOUND_EFFECT_ENABLED="/api/audio/sound_effect/enabled/set";
	public static final String API_AUDIO_NOISE_CANCELLATION_ENABLED_SET = "/api/audio/noise_cancellation/enabled/set";


	public static byte[] getVersion(){
		return ParrotProtocol.getRequest(GET_VERSION);
	}

    public static byte[] getNoiseCancelStateCommand(){
        return ParrotProtocol.getRequest(GET_NOISE_CANCEL);
    }

	public byte[] getFriendlyName(){
		return ParrotProtocol.getRequest(GET_FRIENDLY_NAME);
	}

	public static byte[] setNoiseCancellationEnabled(String value) {
		return ParrotProtocol.setRequest(API_AUDIO_NOISE_CANCELLATION_ENABLED_SET, value);
	}

	public static byte[] setSoundEffectEnabled(String value) {
		return ParrotProtocol.setRequest(SET_SOUND_EFFECT_ENABLED, value);
	}





}
