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

void main() {

	vec3 sunDir = normalize(sunL.Direction);
	float sunAmount = max(dot(surfaceNormal, -sunDir), 0);
	vec3 sun = sunL.Colour * sunL.Intensity * sunAmount;


	vec3 totalLight = ((ambL.Colour * ambL.Intensity) + sun) / 2.0f;

	vec4 colour;
	if (useTextures == 1.0) {
		colour = texture(tex, passTexCoord);
	} else {
		colour = baseColour;
	}
	colour = colour * vec4(totalLight, 1.0);

	outColour = colour;

}


