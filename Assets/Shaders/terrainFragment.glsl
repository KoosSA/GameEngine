#version 460

in vec2 passTexCoord;
in vec3 surfaceNormal;
in vec3 toLightVector;

out vec4 outColour;

uniform float useTextures;
uniform vec4 baseColour;
uniform vec3 lightColour;
uniform vec3 lightPosition;

uniform sampler2D sampler;

void main() {

	vec3 unitNormal = normalize(surfaceNormal);
	//vec3 unitLightVector = normalize(toLightVector);
	vec3 unitLightVector = normalize(lightPosition);
	float ndotl = dot(unitNormal, unitLightVector);
	float brightness = max(ndotl, 0.1f);

	vec4 colour;
	if (useTextures == 1) {
		colour = texture(sampler, passTexCoord);
	} else {
		colour = baseColour;
	}
	colour = colour * vec4(lightColour, 1.0) * brightness;

	outColour = colour;

}
