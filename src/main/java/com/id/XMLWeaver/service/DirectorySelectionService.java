package com.id.XMLWeaver.service;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
@Data
public class DirectorySelectionService {
    private Path baseLineOld;
    private Path baseLineNew;
    private Path customer;
}
