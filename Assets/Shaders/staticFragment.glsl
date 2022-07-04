#version 460

in vec2 passTexCoord;
in vec3 surfaceNormal;
in vec3 toLightVector;

out vec4 outColour;

uniform float useTextures;
uniform vec4 baseColour;
uniform sampler2D tex;
uniform vec3 lightColour;

void main() {

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	float ndotl = dot(unitNormal, unitLightVector);
	float brightness = max(ndotl, 0);

	vec4 colour;
	if (useTextures == 1.0) {
		colour = texture(tex, passTexCoord);
	} else {
		colour = baseColour;
	}
	colour = colour * vec4(lightColour, 1.0) * brightness;

	outColour = colour;

}
