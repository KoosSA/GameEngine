#version 460

in vec2 passTexCoord;
in vec3 surfaceNormal;
in vec3 vertexPos;

out vec4 outColour;

struct ambientLight {
	float Intensity;
	vec3 Colour;
};

struct directionalLight {
	float Intensity;
	vec3 Direction;
	vec3 Colour;
};

uniform float useTextures;
uniform vec4 baseColour;
uniform sampler2D tex;
uniform ambientLight ambL;
uniform directionalLight sunL;
uniform float reflectivity;
uniform float shineDampener;
uniform vec3 camPosition;

void main() {

	vec3 sunDir = normalize(sunL.Direction);
	float sunAmount = max(dot(surfaceNormal, -sunDir), 0);
	vec3 sun = sunL.Colour * sunL.Intensity;
	vec3 toCam = normalize(camPosition - vertexPos);
	vec3 refLightDir = reflect(sunDir, surfaceNormal);
	float sunSpecularFactor = max(dot(refLightDir, toCam), 0);
	float sunDampenedFactor = pow(sunSpecularFactor, shineDampener);

	vec3 totalLight = (ambL.Colour  + sun) * (ambL.Intensity + sunAmount + (sunDampenedFactor * reflectivity)) ;

	vec4 colour;
	if (useTextures == 1.0) {
		colour = texture(tex, passTexCoord);
	} else {
		colour = baseColour;
	}
	colour = colour * vec4(totalLight, 1.0);

	outColour = colour;

}


