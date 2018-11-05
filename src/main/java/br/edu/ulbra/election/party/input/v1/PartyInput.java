package br.edu.ulbra.election.party.input.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel(description = "Party Input Information")
@Data
public class PartyInput {

    @ApiModelProperty(example = "PJ", notes = "Party Code")
    @NotNull
    private String code;
    @ApiModelProperty(example = "Party of Java", notes = "Party Name")
    @NotNull
    private String name;
    @ApiModelProperty(example = "77", notes = "Party Number")
    @NotNull
    private Integer number;

}
