package de.codekeepers.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.codekeepers.domain.Folder;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderResponse {
    public List<Folder> value;
}
